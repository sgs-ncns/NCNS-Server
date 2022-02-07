package com.ncns.sns.post;

import com.ncns.sns.post.dto.request.CreateCommentRequestDto;
import com.ncns.sns.post.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    void createComment() {
        CreateCommentRequestDto dto = new CreateCommentRequestDto(5L, "댓글입니다");
        commentService.createComment(dto);
    }
}
