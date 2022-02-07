package com.ncns.sns.post.controller;


import com.ncns.sns.post.dto.request.CreateCommentRequestDto;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdateCommentRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.service.CommentService;
import com.ncns.sns.post.service.PostService;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.domain.ResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
@RestController
public class PostController {

    @Value("${server.port}")
    private String port;

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createPost(@Validated @RequestBody CreatePostRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.failureResponse(port, ResponseType.USER_VALIDATION_FAILURE, errors);
        }
        postService.createPost(dto);
        return ResponseEntity.successResponse(port);
    }

    @PatchMapping("/{postId")
    public ResponseEntity<Void> updatePost(@RequestBody UpdatePostRequestDto dto) {
        postService.updatePost(dto);
        return ResponseEntity.successResponse(port);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.successResponse(port);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@PathVariable Long userId) {
        List<PostResponseDto> response = postService.getUserPosts(userId);
        return ResponseEntity.successResponse(port, response);
    }

    @GetMapping("/{userId}/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long userId, Long postId) {
        PostDetailResponseDto response = PostDetailResponseDto
                .of(postService.getPostDetail(postId), commentService.getCommentList(postId));
        return ResponseEntity.successResponse(port, response);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> createComment(@Validated @RequestBody CreateCommentRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.failureResponse(port, ResponseType.USER_VALIDATION_FAILURE, errors);
        }
        commentService.createComment(dto);
        return ResponseEntity.successResponse(port);
    }

    @PatchMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@RequestBody UpdateCommentRequestDto dto) {
        commentService.updateComment(dto);
        return ResponseEntity.successResponse(port);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, Long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.successResponse(port);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        String data = postService.requestLikePost(postId);
        return ResponseEntity.successResponse(port, data);
    }
}