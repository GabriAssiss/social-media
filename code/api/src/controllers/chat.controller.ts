import type { Request, Response } from 'express';
import { Message } from '../../mongoose/models/message.model.js';
import prisma from '../database/prisma.client.js';
import { BadRequestError, ServerError, NotFoundError } from '../utils/api-errors.js';

class ChatController {

    async getHistory(req: Request, res: Response) {
        const userId = req.user.id;
        const otherUserId = Number(req.params.otherUserId);
        const page = Number(req.query.page) || 1;
        const limitAmount = 50;

        if (!otherUserId || isNaN(otherUserId)) {
            throw new BadRequestError('ID inválido.');
        }

        const otherUser = await prisma.user.findUnique({
            where: { id: otherUserId }
        });

        if (!otherUser) {
            throw new NotFoundError('Usuário destinatário não existe.');
        }

        const messages = await Message.find({
            $or: [
                { senderId: userId, receiverId: otherUserId },
                { senderId: otherUserId, receiverId: userId }
            ]
        })
            .sort({ createdAt: -1 })
            .limit(limitAmount)
            .skip((page - 1) * limitAmount)
            .lean();

        const normalized = messages.reverse().map((msg: any) => ({
            id: String(msg._id),
            senderId: msg.senderId,
            receiverId: msg.receiverId,
            text: msg.text,
            image: msg.image,
            read: msg.read,
            createdAt: msg.createdAt
        }));

        return res.status(200).json(normalized);
    }

    async getConversations(req: Request, res: Response) {
        const userId = req.user.id;

        const lastMessages = await Message.aggregate([
            {
                $match: {
                    $or: [{ senderId: userId }, { receiverId: userId }]
                }
            },
            { $sort: { createdAt: -1 } },
            {
                $group: {
                    _id: {
                        $cond: [
                            { $eq: ['$senderId', userId] },
                            '$receiverId',
                            '$senderId'
                        ]
                    },
                    lastMessage: { $first: '$$ROOT' }
                }
            }
        ]);

        const userIds = lastMessages.map(m => m._id);
        const users = await prisma.user.findMany({
            where: { id: { in: userIds } },
            select: { id: true, name: true, profile_url: true }
        });

        const usersById = new Map(users.map(user => [user.id, user]));

        const conversations = lastMessages
            .map(m => ({
                user: usersById.get(m._id),
                lastMessage: m.lastMessage
            }))
            .filter(c => c.user !== undefined)
            .sort((a, b) =>
                new Date(b.lastMessage.createdAt).getTime() -
                new Date(a.lastMessage.createdAt).getTime()
            ); 

        return res.status(200).json(conversations);
    }
}

export default new ChatController();