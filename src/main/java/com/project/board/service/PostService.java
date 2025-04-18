package com.project.board.service;

import com.project.board.exception.post.PostNotFoundException;
import com.project.board.exception.user.UserNotAllowedException;
import com.project.board.model.entity.UserEntity;
import com.project.board.model.post.Post;
import com.project.board.model.post.PostPatchRequestBody;
import com.project.board.model.post.PostRequestBody;
import com.project.board.model.entity.PostEntity;
import com.project.board.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;

    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        return Post.from(postEntity);
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
}
