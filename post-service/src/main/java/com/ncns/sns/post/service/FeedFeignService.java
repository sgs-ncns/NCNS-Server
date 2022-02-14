package com.ncns.sns.post.service;

import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.domain.PostCount;
import com.ncns.sns.post.dto.request.FeedPullRequestDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.repository.PostRepository;
import com.ncns.sns.post.repository.PostsCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedFeignService {
    private final PostRepository postRepository;
    private final PostsCountRepository postsCountRepository;

    public List<PostResponseDto> getNewFeeds(FeedPullRequestDto request) {
        List<PostResponseDto> feedList = new ArrayList<>();
        List<Long> followingList = request.getFollowingList();
        followingList.forEach(following -> {
            List<Post> postList =
                    postRepository.findNewPostByUserId(following, request.getLastUpdated());
            if (postList != null)
                feedList.addAll(toResponseDto(postList));
        });
        return feedList;
    }

    public PostResponseDto createSubscribeFeed(Post post) {
        PostCount postCount = postsCountRepository.findByPostId(post.getId());
        return PostResponseDto.of(post, postCount);
    }

    private List<PostResponseDto> toResponseDto(List<Post> postList) {
        return postList.stream().map(post -> {
            PostCount postCount = postsCountRepository.findByPostId(post.getId());
            return PostResponseDto.of(post, postCount);
        }).collect(Collectors.toList());
    }

}