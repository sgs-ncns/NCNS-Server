package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findFollowsByUserId(Long userId);

    @Query("select f.targetId from Follow f where f.userId = :userId")
    List<Long> findTargetIdByUserId(Long userId);

    @Query("select f.userId from Follow f where f.targetId = :targetId")
    List<Long> findUserIdByTargetId(Long targetId);

    Follow findByUserIdAndTargetId(Long userId, Long targetId);

    @Modifying
    @Query("delete from Follow f where f.userId = :userId")
    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("delete from Follow f where f.targetId = :targetId")
    void deleteAllByTargetId(Long targetId);

    boolean existsByUserIdAndTargetId(Long userId, Long targetId);

}
