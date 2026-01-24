import type { NextFunction, Request, Response } from 'express';
import type { ApiError } from '../utils/api-errors.js';

export const errorMiddleware = (err: Error & ApiError, req: Request, res: Response, next: NextFunction) => {
  const statusCode = err.statusCode ?? 500;
  const message = err.message ?? 'Internal Server Error';
  return res.status(statusCode).json({ message});
};

