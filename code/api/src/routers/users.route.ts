import express from 'express';
import usersController from '../controllers/users.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.get('/', (req, res) => usersController.findAll(req, res));

export default router;