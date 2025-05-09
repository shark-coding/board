package com.project.board.controller;

import com.project.board.model.entity.UserEntity;
import com.project.board.model.post.Post;
import com.project.board.model.reply.Reply;
import com.project.board.model.user.*;
import com.project.board.service.PostService;
import com.project.board.service.ReplyService;
import com.project.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private PostService postService;
    @Autowired private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query, Authentication authentication) {
        // 검색어가 없을 때는 모든 유저, 있을 때는 검색어가 포함된 사용자 이름을 갖고 있는 user
        List<User> users = userService.getUsers(query, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username, Authentication authentication) {
        User user = userService.getUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody UserPatchRequestBody userPatchRequestBody,
            Authentication authentication) {
        User user = userService.updateUser(username, userPatchRequestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username, Authentication authentication) {
        List<Post> posts = postService.getPostsByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username, Authentication authentication) {
        User user = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(@PathVariable String username, Authentication authentication) {
        User user = userService.unfollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(@PathVariable String username, Authentication authentication) {
        List<Follower> followers =
                userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable String username, Authentication authentication) {
        List<User> followings =
                userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(@PathVariable String username) {
        List<Reply> replies =
                replyService.getRepliesByUser(username);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(@PathVariable String username, Authentication authentication) {
        List<LikedUser> likedUsers =
                userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(likedUsers);
    }


    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        User user = userService.signUp(
                userSignUpRequestBody.username(),
                userSignUpRequestBody.password()
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {
        UserAuthenticationResponse response = userService.authenticate(
                userLoginRequestBody.username(),
                userLoginRequestBody.password()
        );
        return ResponseEntity.ok(response);
    }


}
