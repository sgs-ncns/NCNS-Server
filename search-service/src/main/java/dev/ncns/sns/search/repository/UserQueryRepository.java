package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQueryRepository {

    void update(User user, String accountName, String nickname);

}
