package com.project.board.service;

import com.project.board.exception.post.PostNotFoundException;
import com.project.board.exception.user.UserNotAllowedException;
import com.project.board.exception.user.UserNotFoundException;
import com.project.board.model.entity.LikeEntity;
import com.project.board.model.entity.UserEntity;
import com.project.board.model.post.Post;
import com.project.board.model.post.PostPatchRequestBody;
import com.project.board.model.post.PostRequestBody;
import com.project.board.model.entity.PostEntity;
import com.project.board.repository.LikeEntityRepository;
import com.project.board.repository.PostEntityRepository;
import com.project.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private LikeEntityRepository likeEntityRepository;

    public List<Post> getPosts(UserEntity currentUser) {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(
                postEntity -> getPostWithLikingStatus(postEntity, currentUser)).toList();
    }

    public Post getPostByPostId(Long postId, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        return getPostWithLikingStatus(postEntity, currentUser);
    }

    private Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {
        boolean isLiking = likeEntityRepository.findByUserAndPost(currentUser, postEntity).isPresent();
        return Post.from(postEntity, isLiking);
    }

    public Post createPost(PostRequestBody postRequestBody, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.save(
                PostEntity.of(postRequestBody.body(), currentUser)
        );
        return Post.from(postEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());
        PostEntity updatePostEntity = postEntityRepository.save(postEntity);
        return Post.from(updatePostEntity);
    }

    public void deletePost(Long postId, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostsByUsername(String username, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);
        return postEntities.stream().map(postEntity -> getPostWithLikingStatus(postEntity, currentUser)).toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));
        Optional<LikeEntity> likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
            return Post.from(postEntityRepository.save(postEntity), false);
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
            return Post.from(postEntityRepository.save(postEntity), true);
        }
    }
}
