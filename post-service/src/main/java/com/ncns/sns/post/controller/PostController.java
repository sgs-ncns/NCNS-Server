package com.ncns.sns.post.controller;


import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.dto.request.*;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.dto.response.StatusResponseDto;
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

    /** 유저 프로필 조회 시 피드 정보를 보여주는 Endpoint 입니다.
     * 유저 프로필은 유저데이터, 게시글 수, 팔로잉/팔로워 수로 이루어진 상단 데이터를 우선적으로 호출합니다.
     * 게시글 수가 0이 아닐 경우에만 post 서버로 요청을 보내 작성 게시글 데이터를 가져옵니다.
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@RequestParam Long userId) {
        List<PostResponseDto> response = postService.getUserPosts(userId);
        return getSuccessResponse(response);
    }

    /** 게시글 상세 조회 시 게시글과 댓글을 포함한 정보를 보여주는 Endpoint 입니다. */
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
    public ResponseEntity<StatusResponseDto> likePost(@PathVariable Long postId) {
        StatusResponseDto data = postService.requestLikePost(postId);
        return getSuccessResponse(data);
    }

    /**
     * 피드 서버로부터 pull 정책(디폴트)을 적용한 경우 Syncronize 요청을 받는 Endpoint 입니다.
     * following list 와 lastUpdatedDate 를 파라미터로 받습니다.
     * 팔로잉하는 유저들의 신규 게시글을 리턴합니다.
     */
    @NonAuthorize
    @PostMapping("/feed")
    public List<PostResponseDto> getNewFeeds(@RequestBody FeedPullRequestDto dto) {
        return feedFeignService.getNewFeeds(dto);
    }
}