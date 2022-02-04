package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Comment;
import com.ncns.sns.post.dto.request.CreateCommentRequestDto;
import com.ncns.sns.post.dto.request.UpdateCommentRequestDto;
import com.ncns.sns.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(CreateCommentRequestDto dto) {
        commentRepository.save(dto.toEntity());
    }

    public void updateComment(UpdateCommentRequestDto dto) {
        Comment comment = checkAuthorization(dto.getCommentId());
        comment.updateComment(dto.getConent());
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = checkAuthorization(commentId);
        commentRepository.delete(comment);
    }

    private Comment checkAuthorization(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (comment.getUserId()!=(SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return comment;
    }
}
