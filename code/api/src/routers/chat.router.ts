import express from 'express';
import chatController from '../controllers/chat.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.use(authMiddleware);

router.get('/conversations', chatController.getConversations);
router.get('/history/:otherUserId', chatController.getHistory);

export default router;