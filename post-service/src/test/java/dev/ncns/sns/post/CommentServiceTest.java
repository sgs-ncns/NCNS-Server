package dev.ncns.sns.post;

import dev.ncns.sns.post.dto.request.CreateCommentRequestDto;
import dev.ncns.sns.post.dto.request.UpdateCommentRequestDto;
import dev.ncns.sns.post.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    void createComment() {
        CreateCommentRequestDto dto = new CreateCommentRequestDto(9L,"ncns","comment comment");
        commentService.createComment(dto);
    }

    @Test
    void updateComment() {
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto(3L,"updated!!!!!!!!!");
        commentService.updateComment(dto);
    }

    @Test
    void deleteComment() {
        commentService.deleteComment(2L);
    }
}
