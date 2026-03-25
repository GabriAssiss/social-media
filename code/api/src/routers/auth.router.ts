import express from 'express';
import AuthController from '../controllers/auth.controller.js';
import { authMiddleware } from '../middlewares/auth.js';

const router = express.Router();

router.post('/', (req, res) => AuthController.create(req, res));
router.post('/login', (req, res) => AuthController.login(req, res));

router.use(authMiddleware);



export default router;