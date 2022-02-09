package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.controller.UserFeignClient;
import com.ncns.sns.post.domain.*;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdateUserPostCountDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.repository.*;
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
    private final HashtagRepository hashtagRepository;
    private final UserTagRepository userTagRepository;

    private final UserFeignClient userFeignClient;

    @Transactional
    public void createPost(CreatePostRequestDto dto) {
        String hashtags = saveHashTag(dto.getHashtag());

        Post post = postRepository.save(dto.toEntity(hashtags));

        if (dto.getUsertag() != null) {
            dto.getUsertag().forEach(userId -> saveUserTag(post.getId(), userId));
        }
        postCountRepository.save(new PostCount(post.getId()));
        // TODO: user post count ++
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(post.getUserId(),true));
    }

    @Transactional
    public void updatePost(UpdatePostRequestDto dto) {
        Post post = checkAuthorization(dto.getPostId());

        userTagRepository.deleteAllByPostId(dto.getPostId());
        dto.getUsertag().forEach(userId -> saveUserTag(dto.getPostId(), userId));

        deleteHashTags(post.getHashtagList());

        String newHashtags = saveHashTag(dto.getHashtag());

        post.updatePost(dto.getContent(), newHashtags);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = checkAuthorization(postId);

        deleteHashTags(post.getHashtagList());

        userTagRepository.deleteAllByPostId(postId);

        PostCount postCount = postCountRepository.getById(postId);
        postCountRepository.deleteById(postCount.getId());

        postRepository.delete(post);

        // TODO: user post count --
        userFeignClient.updateUserPostCount(new UpdateUserPostCountDto(post.getUserId(),false));
    }

    @Transactional(readOnly = true)
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
        return likeData == null ? like(postId) : disLike(likeData, postId);
    }

    private Post checkAuthorization(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (!post.getUserId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return post;
    }

    private String disLike(Long like, Long postId) {
        likeRepository.deleteById(like);
        PostCount postCount = postCountRepository.findByPostId(postId);
        postCount.update(CountType.LIKE, false);
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
        postCount.update(CountType.LIKE, true);
        return "like";
    }

    private void saveUserTag(Long postId, Long userId) {
        UserTag userTag = UserTag.builder()
                .postId(postId)
                .userId(userId)
                .build();
        userTagRepository.save(userTag);
    }

    private String saveHashTag(List<String> hashtagList) {
        return hashtagList == null ? null :
                hashtagList.stream().map(hash -> {
                    Hashtag hashtag = hashtagRepository.findByContent(hash)
                            .orElse(new Hashtag(hash, 0));
                    hashtag.update(true);
                    return hash;
                }).collect(Collectors.joining(","));
    }

    private void deleteHashTags(List<String> hashtagList) {
        hashtagList.forEach(hash -> {
            Hashtag hashtag = hashtagRepository.findByContent(hash)
                    .orElseThrow(() -> new IllegalArgumentException("no hashtag"));
            hashtag.update(false);
        });
    }
}