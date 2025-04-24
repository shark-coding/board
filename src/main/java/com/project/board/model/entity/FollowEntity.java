package com.project.board.model.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "\"follow\"",
        indexes = {@Index(name = "follow_follower_idx", columnList = "follower, following", unique = true)})
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @Column
    private ZonedDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name = "follower")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following")
    private UserEntity following;

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public UserEntity getFollower() {
        return follower;
    }

    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }

    public UserEntity getFollowing() {
        return following;
    }

    public void setFollowing(UserEntity following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowEntity that = (FollowEntity) o;
        return Objects.equals(followId, that.followId) && Objects.equals(createDateTime, that.createDateTime) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId, createDateTime, follower, following);
    }

    public static FollowEntity of(UserEntity follower, UserEntity following) {
        FollowEntity follow = new FollowEntity();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    @PrePersist
    private void prePersist() {
        this.createDateTime = ZonedDateTime.now();
    }
}
