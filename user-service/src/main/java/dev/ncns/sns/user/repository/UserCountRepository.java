package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCountRepository extends JpaRepository<UserCount, Long> {
    UserCount findByUserId(Long userId);

    void deleteByUserId(Long userId);

}