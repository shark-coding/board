package com.project.board.service;

import com.project.board.exception.post.PostNotFoundException;
import com.project.board.exception.reply.ReplyNotFoundException;
import com.project.board.exception.user.UserNotAllowedException;
import com.project.board.exception.user.UserNotFoundException;
import com.project.board.model.entity.PostEntity;
import com.project.board.model.entity.ReplyEntity;
import com.project.board.model.entity.UserEntity;
import com.project.board.model.reply.Reply;
import com.project.board.model.reply.ReplyPatchRequestBody;
import com.project.board.model.reply.ReplyRequestBody;
import com.project.board.repository.PostEntityRepository;
import com.project.board.repository.ReplyEntityRepository;
import com.project.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyEntityRepository replyEntityRepository;
    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        List<ReplyEntity> replyEntity = replyEntityRepository.findByPost(postEntity);
        return replyEntity.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(Long postId, ReplyRequestBody replyRequestBody, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        ReplyEntity replyEntity =
                replyEntityRepository.save(
                        ReplyEntity.of(replyRequestBody.body(), currentUser, postEntity));
        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);
        return Reply.from(replyEntity);
    }

    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {
        postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());
        ReplyEntity updateReplyEntity = replyEntityRepository.save(replyEntity);
        return Reply.from(updateReplyEntity);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }
        replyEntityRepository.delete(replyEntity);
        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
    }

    public List<Reply> getRepliesByUser(String username) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<ReplyEntity> replyEntity = replyEntityRepository.findByUser(userEntity);
        return replyEntity.stream().map(Reply::from).toList();
    }
}
