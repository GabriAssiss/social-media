import express from 'express';
import UserController from '../controllers/users.controller.js';

const router = express.Router();

router.post('/', (req, res) => UserController.create(req, res));

export default router;