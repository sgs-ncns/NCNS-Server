package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
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

    /**
     * 게시글 등록 요청입니다.
     * 게시글에 포함된 이미지 링크, 글 내용, 해시태그 리스트, 계정 태그 리스트를 받습니다.
     * 해시태그는 saveHashTag 메서드에서 생성 혹은 업데이트 처리 후 , 로 concat 되어 String 으로 반환됩니다.
     * 계정 태그는 각각 객체로 변환해 저장합니다.
     * 새 post 와 post count 를 생성합니다.
     */
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

        deleteHashTags(post.getHashtagList());

        String newHashtags = saveHashTag(dto.getHashtag());

        post.updatePost(dto.getContent(), newHashtags);
    }

    /**
     * 게시글 삭제 요청입니다.
     * 삭제 요청자와 게시글 작성자가 동일한지 검토합니다.
     * 유효한 요청이라면 해시태그 테이블을 업데이트하고 해당 게시글의 계정 태그 정보를 전부 삭제합니다.
     * Post 와 Post count 를 삭제 처리 합니다.
     */
    @Transactional
    public void deletePost(Long postId) {
        Post post = checkAuthorization(postId);

        deleteHashTags(post.getHashtagList());

        userTagRepository.deleteAllByPostId(postId);

        PostCount postCount = postCountRepository.getById(postId);
        postCountRepository.deleteById(postCount.getId());

        postRepository.delete(post);
    }

    /**
     * 피드에 사용되는 메서드입니다.
     * Post 와 count 정보가 포함됩니다.
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> getUserPosts(Long userId) {
        return postRepository.findAllByUserId(userId).stream()
                .map(posts -> {
                            PostCount postCount = postCountRepository.findByPostId(posts.getId());
                            return PostResponseDto.of(posts, postCount);
                        }
                ).collect(Collectors.toList());
    }

    /**
     * 게시글 상세보기(댓글 보기)에 사용되는 메서드입니다.
     * count 정보는 제외됩니다.
     */
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ResponseType.POST_NOT_EXIST));
    }

    /**
     * 게시글 좋아요 요청 메서드입니다.
     * 좋아요 상태를 확인 후 토글합니다.
     * 좋아요 정보를 추가/삭제하고, count 를 업데이트합니다.
     */
    @Transactional
    public String requestLikePost(Long postId) {
        Long likeData = likeRepository.isLiked(postId, SecurityUtil.getCurrentMemberId());
        return likeData == null ? like(postId) : disLike(likeData, postId);
    }

    private Post checkAuthorization(Long postId) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new BadRequestException(ResponseType.POST_NOT_AUTHOR);
        }
        return post;
    }

    private String disLike(Long like, Long postId) {
        likeRepository.deleteById(like);
        PostCount postCount = postCountRepository.findByPostId(postId);
        if (postCount.getLikeCount() <= 0) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
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
    /**
     * 요청에 해시태그가 포함되어 있지 않다면 null 을, 있다면 로직 처리 후 String 을 반환합니다.
     * 리스트의 각 해시태그에 대해 기존 데이터 존재 여부를 확인하고 없다면 새 객체를 생성, 있다면 count++을 수행합니다.
     */
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
                    .orElseThrow(() -> new NotFoundException(ResponseType.POST_NOT_EXIST));
            hashtag.update(false);
        });
    }
}