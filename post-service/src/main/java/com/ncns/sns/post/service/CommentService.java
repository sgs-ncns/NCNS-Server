package com.ncns.sns.post.service;

import com.ncns.sns.post.util.SecurityUtil;
import com.ncns.sns.post.domain.Comment;
import com.ncns.sns.post.domain.CountType;
import com.ncns.sns.post.domain.PostCount;
import com.ncns.sns.post.dto.request.CreateCommentRequestDto;
import com.ncns.sns.post.dto.request.UpdateCommentRequestDto;
import com.ncns.sns.post.dto.response.CommentResponseDto;
import com.ncns.sns.post.repository.CommentRepository;
import com.ncns.sns.post.repository.PostsCountRepository;
import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsCountRepository postsCountRepository;

    public List<CommentResponseDto> getCommentList(Long postId) {
        return commentRepository.findAllByPostId(postId)    // TODO: pagination
                .stream().map(CommentResponseDto::new)
                .collect(Collectors.toList());
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
        comment.updateComment(dto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = checkAuthorization(commentId);
        PostCount postCount = postsCountRepository.findByPostId(comment.getPostId());
        if (postCount.getCommentCount() <= 0) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
        commentRepository.delete(comment);
        postCount.update(CountType.COMMENT, false);
    }

    @Transactional
    public void deleteRelatedComment(Long postId) {
        commentRepository.deleteAllByPostId(postId);
    }

    private Comment checkAuthorization(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ResponseType.POST_NOT_EXIST_COMMENT));
        if (!comment.getUserId().equals(SecurityUtil.getCurrentUserId())) {
            throw new BadRequestException(ResponseType.POST_NOT_AUTHOR);
        }
        return comment;
    }
}