package com.ncns.sns.post.service;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Comment;
import com.ncns.sns.post.domain.CountType;
import com.ncns.sns.post.domain.PostCount;
import com.ncns.sns.post.dto.request.CreateCommentRequestDto;
import com.ncns.sns.post.dto.request.UpdateCommentRequestDto;
import com.ncns.sns.post.repository.CommentRepository;
import com.ncns.sns.post.repository.PostsCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsCountRepository postsCountRepository;

    public List<Comment> getCommentList(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Transactional
    public void createComment(CreateCommentRequestDto dto) {
        commentRepository.save(dto.toEntity());
        PostCount postCount = postsCountRepository.findByPostId(dto.getPostId());
        postCount.update(CountType.COMMENT, true);
    }

    @Transactional
    public void updateComment(UpdateCommentRequestDto dto) {
        Comment comment = checkAuthorization(dto.getCommentId());
        comment.updateComment(dto.getConent());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = checkAuthorization(commentId);
        commentRepository.delete(comment);
        PostCount postCount = postsCountRepository.findByPostId(postId);
        postCount.update(CountType.COMMENT, false);
    }

    private Comment checkAuthorization(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("no such post"));
        if (comment.getUserId() != (SecurityUtil.getCurrentMemberId())) {
            throw new IllegalArgumentException("not authorized");
        }
        return comment;
    }
}