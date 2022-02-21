package dev.ncns.sns.post.dto.response;

import dev.ncns.sns.post.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long userId;
    private String accountName;
    private String content;
    private Long parentId;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        BeanUtils.copyProperties(comment, this);
    }

}
