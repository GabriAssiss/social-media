import usersRouter from './routers/users.router.js';
import express from 'express';
import { errorMiddleware } from './middlewares/error.js';

const app = express();

app.use(express.json());

app.use('/api/v1/user/', usersRouter);

app.use(errorMiddleware)

export default app;