// chat.controller.ts
import type { Request, Response } from 'express'
import chatService from '../services/chat.service.js'
import { BadRequestError } from '../utils/api-errors.js'

class ChatController {

    async getHistory(req: Request, res: Response) {
        const userId = req.user.id
        const otherUserId = Number(req.params.otherUserId)
        const before = req.query.before ? new Date(req.query.before as string) : undefined

        if (!otherUserId || isNaN(otherUserId)) {
            throw new BadRequestError('ID inválido.')
        }

        const messages = await chatService.getHistory(userId, otherUserId, before)
        return res.status(200).json(messages)
    }

    async getConversations(req: Request, res: Response) {
        const userId = req.user.id
        const conversations = await chatService.getConversations(userId)
        return res.status(200).json(conversations)
    }
}

export default new ChatController()