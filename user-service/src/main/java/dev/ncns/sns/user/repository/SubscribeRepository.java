package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("select s.targetId from Subscribe s where s.userId = :userId")
    List<Long> findTargetIdByUserId(Long userId);

    Subscribe findByUserIdAndTargetId(Long userId, Long targetId);

}
