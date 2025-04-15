package com.project.board.service;

import com.project.board.model.Post;
import com.project.board.model.PostPatchRequestBody;
import com.project.board.model.PostRequestBody;
import com.project.board.model.entity.PostEntity;
import com.project.board.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return Post.from(postEntity);
    }

    public Post createPost(PostRequestBody postRequestBody) {
        PostEntity postEntity = new PostEntity();
        postEntity.setBody(postRequestBody.body());
        PostEntity savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        postEntity.setBody(postPatchRequestBody.body());
        PostEntity updatePostEntity = postEntityRepository.save(postEntity);
        return Post.from(updatePostEntity);
    }

    public void deletePost(Long postId) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        postEntityRepository.delete(postEntity);
    }
}
