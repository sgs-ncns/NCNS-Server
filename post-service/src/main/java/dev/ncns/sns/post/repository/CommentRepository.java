package dev.ncns.sns.post.repository;

import dev.ncns.sns.post.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    @Modifying
    @Query("delete from Comment c where c.postId = :postId")
    void deleteAllByPostId(Long postId);

}
