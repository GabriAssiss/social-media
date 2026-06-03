import { Message } from '../../mongoose/models/message.model.js'
import prisma from '../database/prisma.client.js'

export class ChatRepository {

    async findMessages(userId: number, otherUserId: number, before?: Date, limit = 50) {
        const query: any = {
            $or: [
                { senderId: userId, receiverId: otherUserId },
                { senderId: otherUserId, receiverId: userId }
            ]
        }

        if (before) {
            query.createdAt = { $lt: before }
        }

        const messages = await Message.find(query)
            .sort({ createdAt: -1 })
            .limit(limit)
            .lean()

        return messages.reverse()
    }

    async findUserById(id: number) {
        return prisma.user.findUnique({ where: { id } })
    }

    async findLastMessagesByUser(userId: number) {
        return Message.aggregate([
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
        ])
    }

    async findUsersByIds(ids: number[]) {
        return prisma.user.findMany({
            where: { id: { in: ids } },
            select: { id: true, name: true, profile_url: true }
        })
    }
}

export default new ChatRepository()