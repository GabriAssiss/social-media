import type { Request, Response } from 'express'
import chatRepository from '../repositories/chat.repository.js'
import { BadRequestError, NotFoundError } from '../utils/api-errors.js'

class ChatController {

    async getHistory(req: Request, res: Response) {
        const userId = req.user.id
        const otherUserId = Number(req.params.otherUserId)
        const before = req.query.before ? new Date(req.query.before as string) : undefined

        if (!otherUserId || isNaN(otherUserId)) {
            throw new BadRequestError('ID inválido.')
        }

        const otherUser = await chatRepository.findUserById(otherUserId)
        if (!otherUser) throw new NotFoundError('Usuário destinatário não existe.')

        const messages = await chatRepository.findMessages(userId, otherUserId, before)

        const normalized = messages.map((msg: any) => ({
            id: String(msg._id),
            senderId: msg.senderId,
            receiverId: msg.receiverId,
            text: msg.text,
            image: msg.image,
            read: msg.read,
            createdAt: msg.createdAt
        }))

        return res.status(200).json(normalized)
    }

    async getConversations(req: Request, res: Response) {
        const userId = req.user.id

        const lastMessages = await chatRepository.findLastMessagesByUser(userId)
        const userIds = lastMessages.map((m: any) => m._id)
        const users = await chatRepository.findUsersByIds(userIds)

        const usersById = new Map(users.map(user => [user.id, user]))

        const conversations = lastMessages
            .map((m: any) => ({
                user: usersById.get(m._id),
                lastMessage: m.lastMessage
            }))
            .filter((c: any) => c.user !== undefined)
            .sort((a: any, b: any) =>
                new Date(b.lastMessage.createdAt).getTime() -
                new Date(a.lastMessage.createdAt).getTime()
            )

        return res.status(200).json(conversations)
    }
}

export default new ChatController()