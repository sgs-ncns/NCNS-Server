package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Comments;
import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.domain.PostCount;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.repository.CommentRepository;
import com.ncns.sns.post.repository.PostRepository;
import com.ncns.sns.post.repository.PostsCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostsCountRepository postCountRepository;
    private final CommentRepository commentRepository;

    public void createPost(CreatePostRequestDto dto) {
        postRepository.save(dto.toEntity());
    }

    public void updatePost(UpdatePostRequestDto dto) {
        Post post = checkAuthorization(dto.getPostId());
        post.updatePost(dto.getContent(), dto.getHashtag());
    }

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
        List<Comments> commentsList = commentRepository.findAllByPostId(postId);
        return PostDetailResponseDto.of(post, commentsList);
    }

    private Post checkAuthorization(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (!post.getUserId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return post;
    }
}