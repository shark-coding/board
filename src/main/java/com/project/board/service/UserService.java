package com.project.board.service;

import com.project.board.exception.follow.FollowAlreadyExistsException;
import com.project.board.exception.follow.FollowNotFoundException;
import com.project.board.exception.follow.InvalidFollowException;
import com.project.board.exception.user.PasswordNotMatchException;
import com.project.board.exception.user.UserAlreadyExistsException;
import com.project.board.exception.user.UserNotFoundException;
import com.project.board.model.entity.FollowEntity;
import com.project.board.model.entity.UserEntity;
import com.project.board.model.user.User;
import com.project.board.model.user.UserAuthenticationResponse;
import com.project.board.model.user.UserPatchRequestBody;
import com.project.board.repository.FollowEntityRepository;
import com.project.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private FollowEntityRepository followEntityRepository;

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

    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;
        if (query != null && !query.isBlank()) {
            // query 검색어 기반, 해당 검색어가 username에 포함되어 있는 유저 목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream().map(User::from).toList();
    }

    public User getUser(String username) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return User.from(userEntity);
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

        return User.from(following);
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

        return User.from(following);
    }
}
