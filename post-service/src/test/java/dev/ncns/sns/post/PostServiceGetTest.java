package dev.ncns.sns.post;

import dev.ncns.sns.post.dto.request.CreateCommentRequestDto;
import dev.ncns.sns.post.dto.request.CreatePostRequestDto;
import dev.ncns.sns.post.dto.response.PostDetailResponseDto;
import dev.ncns.sns.post.dto.response.PostResponseDto;
import dev.ncns.sns.post.service.CommentService;
import dev.ncns.sns.post.service.PostService;
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
        CreatePostRequestDto dto = new CreatePostRequestDto();
        postService.createPost(dto);
        CreateCommentRequestDto cdto = new CreateCommentRequestDto(9L, "ncns", "comment comment");
        commentService.createComment(cdto);
    }

    @Test
    void getPostList() {
        List<PostResponseDto> result = postService.getUserPosts(2L);
        result.forEach(System.out::println);
    }

    @Test
    void getPostDetail() {
        PostDetailResponseDto dto = PostDetailResponseDto
                .of(postService.getPostResponse(9L), commentService.getCommentList(9L));
    }

}