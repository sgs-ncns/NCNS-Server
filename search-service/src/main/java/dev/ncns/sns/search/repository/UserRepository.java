package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, Long> {

}
