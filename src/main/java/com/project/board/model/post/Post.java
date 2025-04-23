package com.project.board.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.board.model.entity.PostEntity;
import com.project.board.model.user.User;

import java.time.ZonedDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
        Long postId,
        String body,
        Long repliesCount,
        User user,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime,
        ZonedDateTime deleteDateTime) {
    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getRepliesCount(),
                com.project.board.model.user.User.from(postEntity.getUser()),
                postEntity.getCreateDateTime(),
                postEntity.getUpdateDateTime(),
                postEntity.getDeleteDateTime());
    }
}