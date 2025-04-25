package com.project.board.service;

import com.project.board.exception.follow.FollowAlreadyExistsException;
import com.project.board.exception.follow.FollowNotFoundException;
import com.project.board.exception.follow.InvalidFollowException;
import com.project.board.exception.post.PostNotFoundException;
import com.project.board.exception.user.PasswordNotMatchException;
import com.project.board.exception.user.UserAlreadyExistsException;
import com.project.board.exception.user.UserNotFoundException;
import com.project.board.model.entity.FollowEntity;
import com.project.board.model.entity.LikeEntity;
import com.project.board.model.entity.PostEntity;
import com.project.board.model.entity.UserEntity;
import com.project.board.model.user.*;
import com.project.board.repository.FollowEntityRepository;
import com.project.board.repository.LikeEntityRepository;
import com.project.board.repository.PostEntityRepository;
import com.project.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private FollowEntityRepository followEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private LikeEntityRepository likeEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User signUp(String username, String password) {
        userEntityRepository
                .findByUsername(username)
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        });
        UserEntity userEntity =
                userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            // JWT 발급
            String accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new PasswordNotMatchException();
        }
    }

    public List<User> getUsers(String query, UserEntity currentUser) {
        List<UserEntity> userEntities;
        if (query != null && !query.isBlank()) {
            // query 검색어 기반, 해당 검색어가 username에 포함되어 있는 유저 목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream().map(
                userEntity -> getUserWithFollowingStatus(userEntity, currentUser)).toList();
    }

    public User getUser(String username, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return getUserWithFollowingStatus(userEntity, currentUser);
    }

    private User getUserWithFollowingStatus(UserEntity userEntity, UserEntity currentUser) {
        boolean isFollowing =
                followEntityRepository.findByFollowerAndFollowing(currentUser, userEntity).isPresent();
        return User.from(userEntity, isFollowing);
    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotFoundException();
        }

        if (userPatchRequestBody.description() != null) {
            userEntity.setDescription(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        UserEntity following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot follow themselves");
        }

        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(
                        follow -> {
                            throw new FollowAlreadyExistsException(currentUser, following);
                        });
        followEntityRepository.save(
                FollowEntity.of(currentUser, following));

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(following.getFollowersCount() + 1);

        userEntityRepository.save(following);
        userEntityRepository.save(currentUser);
        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following, true);
    }

    @Transactional
    public User unfollow(String username, UserEntity currentUser) {
        UserEntity following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot unfollow themselves");
        }

        FollowEntity followEntity = followEntityRepository
                .findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(
                        () -> new FollowNotFoundException(currentUser, following));


        followEntityRepository.delete(followEntity);

        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        currentUser.setFollowingsCount(Math.max(0, currentUser.getFollowingsCount() - 1));

        userEntityRepository.save(following);
        userEntityRepository.save(currentUser);
        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following, false);
    }

    // username을 팔로우하는 모든 팔로워들
    public List<Follower> getFollowersByUsername(String username, UserEntity currentUser) {
        UserEntity following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<FollowEntity> followEntities = followEntityRepository.findByFollowing(following);
        return followEntities.stream().map(
                follow -> Follower.from(
                        getUserWithFollowingStatus(follow.getFollower(), currentUser),
                        follow.getCreateDateTime())).toList();
    }

    // username이 팔로잉 하는 목록
    public List<User> getFollowingsByUsername(String username, UserEntity currentUser) {
        UserEntity follower = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<FollowEntity> followEntities = followEntityRepository.findByFollower(follower);
        return followEntities.stream().map(
                follow -> getUserWithFollowingStatus(follow.getFollowing(), currentUser)).toList();
    }

    public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));
        List<LikeEntity> likeEntities = likeEntityRepository.findByPost(postEntity);
        return likeEntities.stream().map(
                likeEntity -> getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser)).toList();
    }

    private LikedUser getLikedUserWithFollowingStatus(
            LikeEntity likeEntity, PostEntity postEntity, UserEntity currentUser) {
        UserEntity likedUserEntity = likeEntity.getUser();
        User userWithFollowingStatus = getUserWithFollowingStatus(likedUserEntity, currentUser);
        return LikedUser.from(
                userWithFollowingStatus,
                postEntity.getPostId(),
                likeEntity.getCreateDateTime());
    }

    public List<LikedUser> getLikedUsersByUser(String username, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);

        return postEntities.stream()
                .flatMap(
                        postEntity ->
                                likeEntityRepository.findByPost(postEntity).stream()
                                        .map(
                                                likeEntity ->
                                                        getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser)))
                .toList();
    }
}
