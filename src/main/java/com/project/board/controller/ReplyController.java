package com.project.board.controller;

import com.project.board.model.entity.UserEntity;
import com.project.board.model.post.Post;
import com.project.board.model.post.PostPatchRequestBody;
import com.project.board.model.post.PostRequestBody;
import com.project.board.model.reply.Reply;
import com.project.board.model.reply.ReplyPatchRequestBody;
import com.project.board.model.reply.ReplyRequestBody;
import com.project.board.service.PostService;
import com.project.board.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }


    @PostMapping
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId,
            @RequestBody ReplyRequestBody replyRequestBody, Authentication authentication) {
        Reply reply = replyService.createReply(postId, replyRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication) {

        Reply reply = replyService.updateReply(postId, replyId, replyPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long postId, @PathVariable Long replyId, Authentication authentication) {
        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }
}
