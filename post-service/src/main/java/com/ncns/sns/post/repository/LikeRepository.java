package com.ncns.sns.post.repository;

import com.ncns.sns.post.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like,Long> {
    @Query(value = "select l.id from likes l where l.post_id = ? and l.user_id = ?", nativeQuery = true)
    Long isLiked(Long postId, Long userId);

    @Modifying
    @Query("delete from Like l where l.postId = :postId")
    void deleteAllByPostId(Long postId);
}