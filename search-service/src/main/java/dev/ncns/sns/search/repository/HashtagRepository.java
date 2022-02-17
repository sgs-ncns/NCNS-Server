package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.Hashtag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface HashtagRepository extends ElasticsearchRepository<Hashtag, Long> {

    Optional<Hashtag> getByContent(String content);

    Hashtag findByContent(String content);

}
