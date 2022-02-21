package com.ncns.sns.post.repository;

import com.ncns.sns.post.domain.PostCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsCountRepository extends JpaRepository<PostCount, Long> {
    PostCount findByPostId(Long postId);

    void deleteByPostId(Long postId);
}