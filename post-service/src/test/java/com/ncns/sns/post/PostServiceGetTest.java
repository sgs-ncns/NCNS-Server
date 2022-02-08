package com.ncns.sns.post;

import com.ncns.sns.post.dto.request.CreateCommentRequestDto;
import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.response.PostDetailResponseDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import com.ncns.sns.post.service.CommentService;
import com.ncns.sns.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostServiceGetTest {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Test
    void createPost() {
        CreatePostRequestDto dto =  new CreatePostRequestDto("2022-02-08!!!");
        postService.createPost(dto);
        CreateCommentRequestDto cdto = new CreateCommentRequestDto(9L,"comment comment");
        commentService.createComment(cdto);
    }

    @Test
    void getPostList() {
        List<PostResponseDto> result = postService.getUserPosts(2L);
        result.forEach(dto -> System.out.println(dto));
    }

    @Test
    void getPostDetail() {
        PostDetailResponseDto dto = PostDetailResponseDto
                .of(postService.getPostDetail(9L),commentService.getCommentList(9L));
//        dto.getCommentList().forEach(comment -> System.out.println(comment.getContent()));
    }
}