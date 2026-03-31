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

export interface ProfileResponse {
  id: number;
  name: string;
  followersCount: number;
  followedCount: number;
}



