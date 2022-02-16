package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCountRepository extends JpaRepository<UserCount, Long> {

    UserCount findByUserId(Long userId);

    void deleteByUserId(Long userId);

    /**
     * 일반적으로 one to many Join 으로 생성되는 컬럼이지만, 저희는 해당 로직을 위해 join 을 걸지 않았습니다.
     * Join cascade 는 delete 쿼리가 여러 번 실행되기 때문에 DB의 부담을 줄이기 위해 join 을 사용하지 않고 Query 로 벌크 요청을 보냈습니다.
     */
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
