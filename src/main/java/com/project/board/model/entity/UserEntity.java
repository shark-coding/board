package com.project.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(
        name = "\"user\"",
        indexes = {@Index(name = "user_username_idx", columnList = "username", unique = true)})
@SQLDelete(sql = "UPDATE \"user\" SET deletedatetime = CURRENT_TIMESTAMP WHERE userid = ?")
@SQLRestriction("deletedatetime IS NULL")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String profile;

    @Column
    private String description;

    @Column
    private ZonedDateTime createDateTime;

    @Column
    private ZonedDateTime updateDateTime;

    @Column
    private ZonedDateTime deleteDateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(profile, that.profile) && Objects.equals(description, that.description) && Objects.equals(createDateTime, that.createDateTime) && Objects.equals(updateDateTime, that.updateDateTime) && Objects.equals(deleteDateTime, that.deleteDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, profile, description, createDateTime, updateDateTime, deleteDateTime);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserEntity of(String username, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        // Avater Placeholder 서비스(https://acater-placeholder.iran.liara.run) 기반
        // 랜덤한 프로필 사진 설정(1~100)
//        userEntity.setProfile("https://avater.iran.liara.run/public/" + (new Random().nextInt(100) + 1));
        userEntity.setProfile("https://picsum.photos/200/300?random=" + (new Random().nextInt(100) + 1));

        return userEntity;
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
