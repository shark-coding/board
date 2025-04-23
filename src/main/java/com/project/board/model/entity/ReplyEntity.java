package com.project.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "reply",
        indexes = {@Index(name = "reply_userid_idx", columnList = "userid"),
                @Index(name = "reply_postid_idx", columnList = "postid")})
@SQLDelete(sql = "UPDATE \"reply\" SET deletedatetime = CURRENT_TIMESTAMP WHERE replyid = ?")
@SQLRestriction("deletedatetime IS NULL")
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private ZonedDateTime createDateTime;

    @Column
    private ZonedDateTime updateDateTime;

    @Column
    private ZonedDateTime deleteDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public ZonedDateTime getDeleteDateTime() {
        return deleteDateTime;
    }

    public void setDeleteDateTime(ZonedDateTime deleteDateTime) {
        this.deleteDateTime = deleteDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplyEntity that = (ReplyEntity) o;
        return Objects.equals(replyId, that.replyId) && Objects.equals(body, that.body) && Objects.equals(createDateTime, that.createDateTime) && Objects.equals(updateDateTime, that.updateDateTime) && Objects.equals(deleteDateTime, that.deleteDateTime) && Objects.equals(user, that.user) && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId, body, createDateTime, updateDateTime, deleteDateTime, user, post);
    }

    public static ReplyEntity of(String body, UserEntity user, PostEntity post) {
        ReplyEntity reply = new ReplyEntity();
        reply.setBody(body);
        reply.setUser(user);
        reply.setPost(post);
        return reply;
    }

    @PrePersist
    private void prePersist() {
        this.createDateTime = ZonedDateTime.now();
        this.updateDateTime = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updateDateTime = ZonedDateTime.now();
    }
}
