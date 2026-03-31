import authRouter from './routers/auth.router.js';
import followRouter from './routers/follows.router.js';
import userRouter from './routers/users.route.js';
import express from 'express';
import { errorMiddleware } from './middlewares/error.js';

const app = express();

app.use(express.json());
app.use('/api/v1/auth/', authRouter);
app.use(errorMiddleware);
app.use('/api/v1/users/', userRouter);
app.use('/api/v1/follows/', followRouter);

export default app;