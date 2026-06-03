// chat.service.ts
import chatRepository from '../repositories/chat.repository.js'
import { NotFoundError } from '../utils/api-errors.js'

export class ChatService {

    async getHistory(userId: number, otherUserId: number, before?: Date) {
        const otherUser = await chatRepository.findUserById(otherUserId)
        if (!otherUser) throw new NotFoundError('Usuário destinatário não existe.')

        const messages = await chatRepository.findMessages(userId, otherUserId, before)

        return messages.map((msg: any) => ({
            id: String(msg._id),
            senderId: msg.senderId,
            receiverId: msg.receiverId,
            text: msg.text,
            image: msg.image,
            read: msg.read,
            createdAt: msg.createdAt
        }))
    }

    async getConversations(userId: number) {
        const lastMessages = await chatRepository.findLastMessagesByUser(userId)
        const userIds = lastMessages.map((m: any) => m._id)
        const users = await chatRepository.findUsersByIds(userIds)

        const usersById = new Map(users.map(user => [user.id, user]))

        return lastMessages
            .map((m: any) => ({
                user: usersById.get(m._id),
                lastMessage: m.lastMessage
            }))
            .filter((c: any) => c.user !== undefined)
            .sort((a: any, b: any) =>
                new Date(b.lastMessage.createdAt).getTime() -
                new Date(a.lastMessage.createdAt).getTime()
            )
    }
}

export default new ChatService()