package com.ncns.sns.post.repository;

import com.ncns.sns.post.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByPostId(Long postId);
}