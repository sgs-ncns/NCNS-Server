package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCountRepository extends JpaRepository<UserCount, Long> {

    UserCount findByUserId(Long userId);

    void deleteByUserId(Long userId);

    @Modifying
    @Query(value = "update UserCount u set u.followerCount = u.followerCount - 1 where u.userId in :userId")
    void updateDecreaseFollowerCountByUserId(List<Long> userId);

    @Modifying
    @Query(value = "update UserCount u set u.followingCount = u.followingCount - 1 where u.userId in :userId")
    void updateDecreaseFollowingCountByUserId(List<Long> userId);

    @Modifying
    @Query(value = "update UserCount u set u.subscribingCount = u.subscribingCount - 1 where u.userId in :userId")
    void updateDecreaseSubscribingCountByUserId(List<Long> userId);

}
