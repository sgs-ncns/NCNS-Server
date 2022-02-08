package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findFollowsByUserId(Long userId);

    @Query("select f.targetId from Follow f where f.userId = :userId")
    List<Long> findTargetIdByUserId(Long userId);

    @Query("select f.userId from Follow f where f.targetId = :userId")
    List<Long> findUserIdByTargetId(Long userId);

    @Query(value = "select f.id from follow f " +
            "where f.user_id = ? and f.target_id = ? " +
            "limit 1", nativeQuery = true)
    Long findFollowId(Long userId, Long targetId);
}
