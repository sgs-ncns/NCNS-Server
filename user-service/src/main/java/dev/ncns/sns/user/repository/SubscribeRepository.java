package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("select s.targetId from Subscribe s where s.userId = :userId")
    List<Long> findTargetIdByUserId(Long userId);

    @Query("select s.userId from Subscribe s where s.targetId = :targetId")
    List<Long> findUserIdByTargetId(Long targetId);

    Subscribe findByUserIdAndTargetId(Long userId, Long targetId);

    /**
     * Method 이름으로 만든 JPQL을 이용하면 그 개수만큼 delete 쿼리가 날라가기 때문에,
     * 하나의 쿼리로 처리하기 위하여 직접 Query를 설정하였습니다.
     */
    @Modifying
    @Query("delete from Subscribe s where s.userId = :userId")
    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("delete from Subscribe s where s.targetId = :targetId")
    void deleteAllByTargetId(Long targetId);

    boolean existsByUserIdAndTargetId(Long userId, Long targetId);

}
