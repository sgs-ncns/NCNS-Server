package com.ncns.sns.post;

import com.ncns.sns.post.dto.request.CreatePostRequestDto;
import com.ncns.sns.post.dto.request.UpdatePostRequestDto;
import com.ncns.sns.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void createPost() {
        CreatePostRequestDto dto = new CreatePostRequestDto("2022-02-03");
        postService.createPost(dto);
    }

    @Test
    void updatePost() {
        UpdatePostRequestDto dto = new UpdatePostRequestDto(2L, "updated");
        postService.updatePost(dto);
    }

    @Test
    void deletePost() {
        postService.deletePost(1L);
    }

}