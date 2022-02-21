package com.ncns.sns.post.repository;

import com.ncns.sns.post.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    @Modifying
    @Query("delete from UserTag u where u.postId = :postId")
    void deleteAllByPostId(Long postId);
}