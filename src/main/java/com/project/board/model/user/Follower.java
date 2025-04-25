package com.project.board.model.user;

import com.project.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record Follower(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingCount,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime,
        ZonedDateTime followedDateTime,
        Boolean isFollowing) {
        public static Follower from(User user, ZonedDateTime followedDateTime) {
        return new Follower(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingCount(),
                user.createDateTime(),
                user.updateDateTime(),
                followedDateTime,
                user.isFollowing());
    }
}
