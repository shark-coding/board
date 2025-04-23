package com.project.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "\"like\"",
        indexes = {@Index(name = "like_userid_idx", columnList = "userid, postid", unique = true)})
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column
    private ZonedDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
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
        LikeEntity that = (LikeEntity) o;
        return Objects.equals(likeId, that.likeId) && Objects.equals(createDateTime, that.createDateTime) && Objects.equals(user, that.user) && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeId, createDateTime, user, post);
    }

    public static LikeEntity of(UserEntity user, PostEntity post) {
        LikeEntity reply = new LikeEntity();
        reply.setUser(user);
        reply.setPost(post);
        return reply;
    }

    @PrePersist
    private void prePersist() {
        this.createDateTime = ZonedDateTime.now();
    }
}
