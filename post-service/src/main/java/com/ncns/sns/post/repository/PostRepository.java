package com.ncns.sns.post.repository;


import com.ncns.sns.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);

    @Query(value = "select * from Posts p " +
            "where p.user_id = :userId " +
            "and p.created_at > :lastUpdated", nativeQuery = true)
    List<Post> findNewPostByUserId(Long userId, LocalDateTime lastUpdated);
}