package dev.ncns.sns.post.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.post.domain.Post;
import dev.ncns.sns.post.dto.kafka.HashtagRequestDto;
import dev.ncns.sns.post.dto.kafka.UpdateHashtagRequestDto;
import dev.ncns.sns.post.dto.request.*;
import dev.ncns.sns.post.dto.response.LikeResponseDto;
import dev.ncns.sns.post.dto.response.PostDetailResponseDto;
import dev.ncns.sns.post.dto.response.PostResponseDto;
import dev.ncns.sns.post.dto.response.StatusResponseDto;
import dev.ncns.sns.post.service.CommentService;
import dev.ncns.sns.post.service.FeedFeignService;
import dev.ncns.sns.post.service.PostService;
import dev.ncns.sns.post.service.kafka.PostProducerService;
import dev.ncns.sns.post.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
@RestController
public class PostController extends ApiController {

    private final PostService postService;
    private final CommentService commentService;
    private final FeedFeignService feedFeignService;
    private final UserFeignClient userFeignClient;
    private final PostProducerService kafkaService;

    @Operation(summary = "게시물 작성")
    @PostMapping
    public ResponseEntity<Void> createPost(@Validated @RequestBody CreatePostRequestDto createPostRequest) {
        Post post = postService.createPost(createPostRequest);
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(SecurityUtil.getCurrentUserId(), true));

        PostResponseDto postResponse = feedFeignService.createSubscribeFeed(post);
        kafkaService.sendUpdateFeedRequest(postResponse);

        HashtagRequestDto hashtagConsumerRequest = HashtagRequestDto.of(post.getId(), createPostRequest.getHashtag());
        kafkaService.sendCreatePostRequest(hashtagConsumerRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "게시물 수정")
    @PatchMapping
    public ResponseEntity<Void> updatePost(@RequestBody UpdatePostRequestDto updatePostRequest) {
        Post post = postService.getPostById(updatePostRequest.getPostId());
        UpdateHashtagRequestDto deleteHashtagConsumerRequest = UpdateHashtagRequestDto.of(post.getId(), post.getHashtag(), false);
        kafkaService.sendUpdatePostRequest(deleteHashtagConsumerRequest);

        postService.updatePost(updatePostRequest);

        HashtagRequestDto hashtagConsumerRequest = HashtagRequestDto.of(post.getId(), updatePostRequest.getHashtag());
        kafkaService.sendCreatePostRequest(hashtagConsumerRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "게시물 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        postService.deletePost(postId);
        commentService.deleteRelatedComment(postId);
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(SecurityUtil.getCurrentUserId(), false));

        HashtagRequestDto hashtagConsumerRequest = HashtagRequestDto.of(post.getId(), post.getHashtag());
        kafkaService.sendDeletePostRequest(hashtagConsumerRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "사용자의 게시물 목록 조회")
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@RequestParam Long userId) {
        List<PostResponseDto> postResponse = postService.getUserPosts(userId);
        return getSuccessResponse(postResponse);
    }

    @Operation(summary = "게시물 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long postId) {
        PostDetailResponseDto postDetailResponse = PostDetailResponseDto
                .of(postService.getPostResponse(postId), commentService.getCommentList(postId));
        if (postService.isLiking(postId) != null) postDetailResponse.liking();
        return getSuccessResponse(postDetailResponse);
    }

    @Operation(summary = "게시물 댓글 작성")
    @PostMapping("/comment")
    public ResponseEntity<Void> createComment(@Validated @RequestBody CreateCommentRequestDto createCommentRequest) {
        commentService.createComment(createCommentRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "게시물 댓글 수정")
    @PatchMapping("/comment")
    public ResponseEntity<Void> updateComment(@RequestBody UpdateCommentRequestDto updateCommentRequest) {
        commentService.updateComment(updateCommentRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "게시물 댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return getSuccessResponse();
    }

    @Operation(summary = "좋아요/좋아요취소 요청", description = "좋아요 중인 경우 -> 좋아요취소 요청, 좋아요 중이 아닌 경우 -> 좋아요 요청")
    @PostMapping("/like/{postId}")
    public ResponseEntity<StatusResponseDto> likePost(@PathVariable Long postId) {
        StatusResponseDto statusResponse = postService.requestLikePost(postId);
        kafkaService.sendUpdateLikeRequest(LikeResponseDto.of(SecurityUtil.getCurrentUserId(), postId, statusResponse.getStatus()));
        return getSuccessResponse(statusResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/feed")
    public List<PostResponseDto> getNewFeeds(@RequestBody FeedPullRequestDto feedPullRequest) {
        return feedFeignService.getNewFeeds(feedPullRequest);
    }

}
