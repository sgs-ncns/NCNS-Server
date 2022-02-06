package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Comment;
import com.ncns.sns.post.domain.Like;
import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.domain.PostCount;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.repository.CommentRepository;
import com.ncns.sns.post.repository.LikeRepository;
import com.ncns.sns.post.repository.PostRepository;
import com.ncns.sns.post.repository.PostsCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostsCountRepository postCountRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void createPost(CreatePostRequestDto dto) {
        postRepository.save(dto.toEntity());
    }

    @Transactional
    public void updatePost(UpdatePostRequestDto dto) {
        Post post = checkAuthorization(dto.getPostId());
        post.updatePost(dto.getContent(), dto.getHashtag());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = checkAuthorization(postId);
        postRepository.delete(post);
    }

    public List<PostResponseDto> getUserPosts(Long userId) {
        return postRepository.findAllByUserId(userId).stream()
                .map(posts -> {
                            PostCount postCount = postCountRepository.findByPostId(posts.getId());
                            return PostResponseDto.of(posts, postCount);
                        }
                ).collect(Collectors.toList());
    }

    public PostDetailResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no post"));
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        return PostDetailResponseDto.of(post, commentList);
    }


    public String requestLikePost(Long postId) {
        Long likeData = likeRepository.isLiked(postId, SecurityUtil.getCurrentMemberId());
        return likeData == null ? like(postId) : disLike(likeData);
    }

    private Post checkAuthorization(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (!post.getUserId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return post;
    }

    private String disLike(Long like) {
        likeRepository.deleteById(like);
        return "disLike";
        //TODO:: like count --
    }

    private String like(Long postId) {
        Like like = Like.builder()
                .postId(postId)
                .userId(SecurityUtil.getCurrentMemberId())
                .build();
        likeRepository.save(like);
        //TODO:: like count ++
        return "like";
    }

}