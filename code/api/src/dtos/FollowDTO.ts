export interface FollowRequest {
  followerId: number;
  followedId: number;
}

export interface UnfollowRequest {
  followerId: number;
  followedId: number;
}