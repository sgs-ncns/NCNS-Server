package com.ncns.sns.post.service;

import com.ncns.sns.post.dto.response.StatusResponseDto;
import com.ncns.sns.post.util.SecurityUtil;
import com.ncns.sns.post.domain.*;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.repository.*;
import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.common.exception.NotFoundException;
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

    @Transactional
    public Post createPost(CreatePostRequestDto dto) {
        String hashtags = saveHashTag(dto.getHashtag());

        Post post = postRepository.save(dto.toEntity(hashtags));

        if (dto.getUsertag() != null) {
            dto.getUsertag().forEach(userId -> saveUserTag(post.getId(), userId));
        }
        postCountRepository.save(new PostCount(post.getId()));
        // TODO: Post 객체 feign -> Kafka Topic 전송으로 전환
        return post;
    }

    @Transactional
    public void updatePost(UpdatePostRequestDto dto) {
        Post post = checkAuthorization(dto.getPostId());

        userTagRepository.deleteAllByPostId(dto.getPostId());
        dto.getUsertag().forEach(userId -> saveUserTag(dto.getPostId(), userId));

        if (!post.getHashtag().isEmpty()) {
            deleteHashTags(post.getHashtagList());
        }
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

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ResponseType.POST_NOT_EXIST));
    }

    @Transactional
    public StatusResponseDto requestLikePost(Long postId) {
        Long likeData = likeRepository.isLiked(postId, SecurityUtil.getCurrentUserId());
        return likeData == null ? like(postId) : disLike(likeData, postId);
    }

    private Post checkAuthorization(Long postId) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(SecurityUtil.getCurrentUserId())) {
            throw new BadRequestException(ResponseType.POST_NOT_AUTHOR);
        }
        return post;
    }

    private StatusResponseDto disLike(Long like, Long postId) {
        likeRepository.deleteById(like);
        PostCount postCount = postCountRepository.findByPostId(postId);
        if (postCount.getLikeCount() <= 0) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
        postCount.update(CountType.LIKE, false);
        return StatusResponseDto.of(LikeStatus.DISLIKE.getValue());
    }

    private StatusResponseDto like(Long postId) {
        Like like = Like.builder()
                .postId(postId)
                .userId(SecurityUtil.getCurrentUserId())
                .build();
        likeRepository.save(like);
        PostCount postCount = postCountRepository.findByPostId(postId);
        postCount.update(CountType.LIKE, true);
        return StatusResponseDto.of(LikeStatus.LIKE.getValue());
    }

    private void saveUserTag(Long postId, Long userId) {
        UserTag userTag = UserTag.builder()
                .postId(postId)
                .userId(userId)
                .build();
        userTagRepository.save(userTag);
    }

    private String saveHashTag(List<String> hashtagList) {
        return hashtagList.size() == 0 ? "" :
                hashtagList.stream().map(hash -> {

                    if (hash.isBlank()) throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);

                    Hashtag hashtag = hashtagRepository.findByContent(hash)
                            .orElseGet(() -> hashtagRepository.save(new Hashtag(hash, 0)));
                    hashtag.update(true);
                    return hash;
                }).collect(Collectors.joining(","));
    }

    private void deleteHashTags(List<String> hashtagList) {
        if (hashtagList == null) return;

        hashtagList.forEach(hash -> {
            Hashtag hashtag = hashtagRepository.findByContent(hash)
                    .orElseThrow(() -> new NotFoundException(ResponseType.POST_NOT_EXIST_HASHTAG));
            hashtag.update(false);
        });
    }
}