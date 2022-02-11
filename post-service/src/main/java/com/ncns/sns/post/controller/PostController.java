package com.ncns.sns.post.controller;


import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.dto.request.*;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.service.CommentService;
import com.ncns.sns.post.service.PostService;
import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.domain.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@ComponentScan(basePackages = "dev.ncns.sns.common.exception")
@RequestMapping(value = "/api/post")
@RestController
public class PostController {

    @Value("${server.port}")
    private String port;

    private final PostService postService;
    private final CommentService commentService;
    private final UserFeignClient userFeignClient;

    @PostMapping
    public ResponseEntity<?> createPost(@Validated @RequestBody CreatePostRequestDto dto) {
        postService.createPost(dto);
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(SecurityUtil.getCurrentMemberId(), true));
        return ResponseEntity.successResponse(port);
    }

    @PatchMapping
    public ResponseEntity<Void> updatePost(@RequestBody UpdatePostRequestDto dto) {
        postService.updatePost(dto);
        return ResponseEntity.successResponse(port);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(SecurityUtil.getCurrentMemberId(), false));
        return ResponseEntity.successResponse(port);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@RequestParam Long userId) {
        List<PostResponseDto> response = postService.getUserPosts(userId);
        return ResponseEntity.successResponse(port, response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long postId) {
        PostDetailResponseDto response = PostDetailResponseDto
                .of(postService.getPostById(postId), commentService.getCommentList(postId));
        return ResponseEntity.successResponse(port, response);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@Validated @RequestBody CreateCommentRequestDto dto) {
        commentService.createComment(dto);
        return ResponseEntity.successResponse(port);
    }

    @PatchMapping("/comment")
    public ResponseEntity<Void> updateComment(@RequestBody UpdateCommentRequestDto dto) {
        commentService.updateComment(dto);
        return ResponseEntity.successResponse(port);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.successResponse(port);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        String data = postService.requestLikePost(postId);
        return ResponseEntity.successResponse(port, data);
    }
}