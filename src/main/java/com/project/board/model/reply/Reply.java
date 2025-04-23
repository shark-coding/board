package com.project.board.model.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.board.model.entity.ReplyEntity;
import com.project.board.model.post.Post;
import com.project.board.model.user.User;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Reply(
        Long replyId,
        String body,
        User user,
        Post post,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime,
        ZonedDateTime deleteDateTime) {
    public static Reply from(ReplyEntity replyEntity) {
        return new Reply(
                replyEntity.getReplyId(),
                replyEntity.getBody(),
                User.from(replyEntity.getUser()),
                Post.from(replyEntity.getPost()),
                replyEntity.getCreateDateTime(),
                replyEntity.getUpdateDateTime(),
                replyEntity.getDeleteDateTime());
    }
}