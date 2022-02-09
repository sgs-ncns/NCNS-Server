package com.ncns.sns.post.repository;

import com.ncns.sns.post.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    void deleteAllByPostId(Long postId);
}
