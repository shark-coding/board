package com.project.board.service;

import com.project.board.exception.post.PostNotFoundException;
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
                                () -> new PostNotFoundException(postId));

        postEntity.setBody(postPatchRequestBody.body());
        PostEntity updatePostEntity = postEntityRepository.save(postEntity);
        return Post.from(updatePostEntity);
    }

    public void deletePost(Long postId) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        postEntityRepository.delete(postEntity);
    }
}
