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

    /**
     * Method 이름으로 만든 JPQL을 이용하면 그 개수만큼 delete 쿼리가 날라가기 때문에,
     * 하나의 쿼리로 처리하기 위하여 직접 Query를 설정하였습니다.
     */
    @Modifying
    @Query("delete from Follow f where f.userId = :userId")
    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("delete from Follow f where f.targetId = :targetId")
    void deleteAllByTargetId(Long targetId);

    boolean existsByUserIdAndTargetId(Long userId, Long targetId);

}
