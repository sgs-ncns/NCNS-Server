package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

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

    private Post checkAuthorization(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (!post.getUserId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return post;
    }
}