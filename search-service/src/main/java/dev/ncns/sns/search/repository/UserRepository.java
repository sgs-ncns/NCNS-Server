package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ElasticsearchRepository<User, Long> {

    Optional<User> getByUserId(Long userId);

    User findByUserId(Long userId);

    List<User> findByAccountNameContains(String keyword);

    List<User> findByNicknameContains(String keyword);

}
