import express from 'express';
import UserController from '../controllers/users.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.post('/', (req, res) => UserController.create(req, res));
router.post('/login', (req, res) => UserController.login(req, res));

router.use(authMiddleware);

router.get('/', (req, res) => UserController.findAll(req, res));

export default router;