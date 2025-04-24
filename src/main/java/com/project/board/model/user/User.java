package com.project.board.model.user;

import com.project.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingCount,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime) {
    public static User from(UserEntity userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
                userEntity.getCreateDateTime(),
                userEntity.getUpdateDateTime());
    }
}
