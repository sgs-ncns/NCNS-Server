package com.ncns.sns.post;

import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.dto.response.StatusResponseDto;
import com.ncns.sns.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void createPost() {
        CreatePostRequestDto dto = new CreatePostRequestDto();
        postService.createPost(dto);
    }

    @Test
    void updatePost() {
        UpdatePostRequestDto dto = new UpdatePostRequestDto();
        postService.updatePost(dto);
    }

    @Test
    void deletePost() {
        postService.deletePost(5L);
    }

    @Test
    void likePost() {
        StatusResponseDto result = postService.requestLikePost(5L);
        assertEquals(result, true);
        result = postService.requestLikePost(5L);
        assertEquals(result.getStatus(), false);

    }
}