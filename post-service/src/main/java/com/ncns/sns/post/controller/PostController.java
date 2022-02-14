package com.ncns.sns.post.controller;


import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.dto.request.*;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.service.CommentService;
import com.ncns.sns.post.service.FeedFeignService;
import com.ncns.sns.post.service.PostService;
import com.ncns.sns.post.util.SecurityUtil;
import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
@RestController
public class PostController extends ApiController {

    private final PostService postService;
    private final CommentService commentService;
    private final FeedFeignService feedFeignService;
    private final UserFeignClient userFeignClient;
    private final FeedFeignClient feedFeignClient;

    @PostMapping
    public ResponseEntity<?> createPost(@Validated @RequestBody CreatePostRequestDto dto) {
        Post post = postService.createPost(dto);
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(SecurityUtil.getCurrentUserId(), true));
        PostResponseDto postDto = feedFeignService.createSubscribeFeed(post);
        feedFeignClient.updateSubscribeFeed(postDto);
        return getSuccessResponse();
    }

    @PatchMapping
    public ResponseEntity<Void> updatePost(@RequestBody UpdatePostRequestDto dto) {
        postService.updatePost(dto);
        return getSuccessResponse();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(SecurityUtil.getCurrentUserId(), false));
        return getSuccessResponse();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@RequestParam Long userId) {
        List<PostResponseDto> response = postService.getUserPosts(userId);
        return getSuccessResponse(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long postId) {
        PostDetailResponseDto response = PostDetailResponseDto
                .of(postService.getPostById(postId), commentService.getCommentList(postId));
        return getSuccessResponse(response);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@Validated @RequestBody CreateCommentRequestDto dto) {
        commentService.createComment(dto);
        return getSuccessResponse();
    }

    @PatchMapping("/comment")
    public ResponseEntity<Void> updateComment(@RequestBody UpdateCommentRequestDto dto) {
        commentService.updateComment(dto);
        return getSuccessResponse();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return getSuccessResponse();
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        String data = postService.requestLikePost(postId);
        return getSuccessResponse(data);
    }

    @NonAuthorize
    @PostMapping("/feed")
    public List<PostResponseDto> getNewFeeds(@RequestBody FeedPullRequestDto dto) {
        List<PostResponseDto> newFeeds = feedFeignService.getNewFeeds(dto);
        return newFeeds;
    }
}