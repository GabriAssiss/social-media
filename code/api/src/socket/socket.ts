import { Server } from 'socket.io';
import type { Server as HttpServer } from 'node:http';
import jwt, { NotBeforeError } from 'jsonwebtoken';
import { Message } from '../../mongoose/models/message.model.js';
import usersRepository from "../repositories/users.repository.js";
import { NotFoundError, UnauthorizedError } from '../utils/api-errors.js';

const onlineUsers = new Map<number, string>();

export function initSocket(server: HttpServer) {
    const io = new Server(server, {
        cors: { origin: '*' }
    });

    io.use(async (socket, next) => {
        const token = socket.handshake.auth.token;

        if (!token) return next(new UnauthorizedError('Token is required'));

        try {
            const payload = jwt.verify(token, process.env.JWT_SECRET!) as { id: number };

            const user = await usersRepository.findById(payload.id);
            if (!user) return next(new NotFoundError('User not found'));

            const { password: _, ...loggedUser } = user;
            socket.data.user = loggedUser;
            next();
        } catch {
            next(new UnauthorizedError('Invalid token'));
        }
    });

    io.on('connection', (socket) => {
        const userId: number = socket.data.user.id;

        socket.join(`user_${userId}`);
        onlineUsers.set(userId, socket.id);
        io.emit('user_online', { userId });
        console.log(`User ${userId} connected`);

        socket.on('send_message', async ({ receiverId, text, image }) => {
            if (!receiverId || (!text?.trim() && !image)) {
                return socket.emit('error', { message: 'Dados inválidos' });
            }
            if (receiverId === userId) {
                return socket.emit('error', { message: 'Não pode enviar mensagem para si mesmo' });
            }
            try {
                const message = await Message.create({ senderId: userId, receiverId, text, image });
                io.to(`user_${receiverId}`).emit('new_message', message);
                socket.emit('message_sent', message);
            } catch (err) {
                socket.emit('error', { message: 'Erro ao enviar mensagem' });
            }
        });

        socket.on('get_messages', async ({ withUserId, page = 1 }) => {
            try {
                const messages = await Message.find({
                    $or: [
                        { senderId: userId, receiverId: withUserId },
                        { senderId: withUserId, receiverId: userId }
                    ]
                })
                .sort({ createdAt: -1 })
                .limit(50)
                .skip((page - 1) * 50);

                socket.emit('messages_history', messages.reverse());
            } catch (err) {
                socket.emit('error', { message: 'Erro ao buscar mensagens' });
            }
        });

        socket.on('read_messages', async ({ senderId }) => {
            try {
                await Message.updateMany(
                    { senderId, receiverId: userId, read: false },
                    { read: true }
                );
                io.to(`user_${senderId}`).emit('messages_read', { by: userId });
            } catch (err) {
                socket.emit('error', { message: 'Erro ao marcar mensagens como lidas' });
            }
        });

        socket.on('disconnect', () => {
            onlineUsers.delete(userId);
            io.emit('user_offline', { userId });
            console.log(`User ${userId} disconnected`);
        });
    });
}