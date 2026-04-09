import type { Request, Response } from 'express';
import { Message } from '../../mongoose/models/message.model.js';
import prisma from '../database/prisma.client.js';
import { BadRequestError, ServerError } from '../utils/api-errors.js';

class ChatController {

    async getHistory(req: Request, res: Response) {
        const userId = req.user.id;
        const otherUserId = Number(req.params.otherUserId);

        if (!otherUserId || isNaN(otherUserId)) {
            throw new BadRequestError('ID inválido.');
        }

        const messages = await Message.find({
            $or: [
                { senderId: userId,      receiverId: otherUserId },
                { senderId: otherUserId, receiverId: userId }
            ]
        }).sort({ createdAt: 1 });

        return res.status(200).json(messages);
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

        const conversations = lastMessages
            .map(m => ({
                user: users.find(u => u.id === m._id),
                lastMessage: m.lastMessage
            }))
            .filter(c => c.user !== undefined);

        return res.status(200).json(conversations);
    }
}

export default new ChatController();