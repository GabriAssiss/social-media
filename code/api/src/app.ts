import authRouter from './routers/auth.router.js';
import followRouter from './routers/follow.router.js';
import express from 'express';
import { errorMiddleware } from './middlewares/error.js';

const app = express();

app.use(express.json());
app.use('/api/v1/auth/', authRouter);
app.use(errorMiddleware);
app.use('/api/v1/users/', followRouter);

export default app;