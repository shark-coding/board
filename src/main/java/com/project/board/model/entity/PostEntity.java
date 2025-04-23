package com.project.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "post",
        indexes = {@Index(name = "post_userid_idx", columnList = "userid")})
@SQLDelete(sql = "UPDATE \"post\" SET deletedatetime = CURRENT_TIMESTAMP WHERE postid = ?")
@SQLRestriction("deletedatetime IS NULL")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private Long repliesCount = 0L;

    @Column
    private Long likesCount = 0L;

    @Column
    private ZonedDateTime createDateTime;

    @Column
    private ZonedDateTime updateDateTime;

    @Column
    private ZonedDateTime deleteDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Long repliesCount) {
        this.repliesCount = repliesCount;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(postId, that.postId) && Objects.equals(body, that.body) && Objects.equals(repliesCount, that.repliesCount) && Objects.equals(likesCount, that.likesCount) && Objects.equals(createDateTime, that.createDateTime) && Objects.equals(updateDateTime, that.updateDateTime) && Objects.equals(deleteDateTime, that.deleteDateTime) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, repliesCount, likesCount, createDateTime, updateDateTime, deleteDateTime, user);
    }

    public static PostEntity of(String body, UserEntity user) {
        PostEntity post = new PostEntity();
        post.setBody(body);
        post.setUser(user);
        return post;

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
