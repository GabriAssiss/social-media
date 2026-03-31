import express from 'express';
import usersController from '../controllers/users.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.use(authMiddleware);

router.get('/me/profile', (req, res) => usersController.myProfile(req, res));
router.get('/:userId/profile', (req, res) => usersController.profile(req, res));

export default router;