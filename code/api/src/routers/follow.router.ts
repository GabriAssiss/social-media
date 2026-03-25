import express from 'express';
import FollowController from '../controllers/follow.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.use(authMiddleware);

router.post('/:followerId/follow', (req, res) => FollowController.follow(req, res));

export default router;