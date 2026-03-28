export interface CreateUserRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
}

export interface LoginUserRequest {
  email: string;
  password: string;
}



