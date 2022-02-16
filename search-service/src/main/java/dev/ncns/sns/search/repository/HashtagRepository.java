package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.Hashtag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HashtagRepository extends ElasticsearchRepository<Hashtag, Long> {

}
