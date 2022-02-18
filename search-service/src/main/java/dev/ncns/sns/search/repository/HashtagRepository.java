package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.Hashtag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends ElasticsearchRepository<Hashtag, Long>, HashtagQueryRepository {

    Optional<Hashtag> getByContent(String content);

    Hashtag findByContent(String content);

    List<Hashtag> findByContentContains(String keyword);

}
