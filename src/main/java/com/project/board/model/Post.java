package com.project.board.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.board.model.entity.PostEntity;

import java.time.ZonedDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
        Long postId,
        String body,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime,
        ZonedDateTime deleteDateTime) {
    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCreateDateTime(),
                postEntity.getUpdateDateTime(),
                postEntity.getDeleteDateTime());
    }
}