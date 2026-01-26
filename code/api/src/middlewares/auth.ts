import { UnauthorizedError, type ApiError } from "../utils/api-errors.js";
import type { Request, Response, NextFunction } from "express";
import jwt from "jsonwebtoken";
import usersRepository from "../repositories/users.repository.js";

type JwtPayload = {
      id: number;
}

export const authMiddleware = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  const { authorization } = req.headers;

  if (!authorization) {
    throw new UnauthorizedError('Authorization header missing.');
  }

  const [scheme, token] = authorization.split(' ');

  if (scheme !== 'Bearer' || !token) {
    throw new UnauthorizedError('Invalid authorization format.');
  }

  let payload: JwtPayload;

  try {
    payload = jwt.verify(
      token,
      process.env.JWT_SECRET!
    ) as JwtPayload;
  } catch {
    throw new UnauthorizedError('Invalid or expired token.');
  }

  const user = await usersRepository.findById(payload.id);

  if (!user) {
    throw new UnauthorizedError('Invalid token.');
  }

  const { password: _, ...loggedUser } = user;

  req.user = loggedUser;
  next();
};
