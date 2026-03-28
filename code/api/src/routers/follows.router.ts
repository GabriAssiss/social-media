import express from 'express';
import FollowController from '../controllers/follows.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.use(authMiddleware);

router.post('/:followerId/', (req, res) => FollowController.follow(req, res));
router.delete('/:followerId/', (req, res) => FollowController.unfollow(req, res));
router.get('/:followerId/followed', (req, res) => FollowController.findAllFollowed(req, res));
router.get('/:followerId/followers', (req, res) => FollowController.findAllFollowers(req, res));

export default router;