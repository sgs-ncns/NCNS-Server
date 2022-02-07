package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.CountType;
import com.ncns.sns.post.domain.Like;
import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.domain.PostCount;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
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
    private final LikeRepository likeRepository;

    @Transactional
    public void createPost(CreatePostRequestDto dto) {
        Post post = postRepository.save(dto.toEntity());
        postCountRepository.save(new PostCount(post.getId()));
        // TODO: user post count ++
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
        PostCount postCount = postCountRepository.getById(postId);
        postCountRepository.deleteById(postCount.getId());
        // TODO: user post count --
    }

    public List<PostResponseDto> getUserPosts(Long userId) {
        return postRepository.findAllByUserId(userId).stream()
                .map(posts -> {
                            PostCount postCount = postCountRepository.findByPostId(posts.getId());
                            return PostResponseDto.of(posts, postCount);
                        }
                ).collect(Collectors.toList());
    }

    public Post getPostDetail(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no post"));
    }

    @Transactional
    public String requestLikePost(Long postId) {
        Long likeData = likeRepository.isLiked(postId, SecurityUtil.getCurrentMemberId());
        return likeData == null ? like(postId) : disLike(likeData,postId);
    }

    private Post checkAuthorization(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (!post.getUserId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return post;
    }

    @Transactional
    private String disLike(Long like,Long postId) {
        likeRepository.deleteById(like);
        PostCount postCount = postCountRepository.findByPostId(postId);
        postCount.update(CountType.LIKE,false);
        return "disLike";
    }

    @Transactional
    private String like(Long postId) {
        Like like = Like.builder()
                .postId(postId)
                .userId(SecurityUtil.getCurrentMemberId())
                .build();
        likeRepository.save(like);
        PostCount postCount = postCountRepository.findByPostId(postId);
        postCount.update(CountType.LIKE,true);
        return "like";
    }

}