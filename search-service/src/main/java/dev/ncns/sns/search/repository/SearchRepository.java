package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.Hashtag;
import dev.ncns.sns.search.domain.User;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class SearchRepository {

    private static final String WILDCARD = "*";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<User> searchUsers(String keyword, Pageable pageable) {
        keyword = WILDCARD + keyword.replace(" ", WILDCARD) + WILDCARD;

        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .should(QueryBuilders.queryStringQuery(keyword).defaultField("accountName"))
                        .should(QueryBuilders.queryStringQuery(keyword).defaultField("nickname")))
                .withPageable(pageable)
                .build();

        SearchHits<User> searchHits = elasticsearchOperations.search(query, User.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public List<Hashtag> searchHashtags(String keyword, Pageable pageable) {
        keyword = WILDCARD + keyword.replace(" ", WILDCARD) + WILDCARD;

        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.queryStringQuery(keyword).defaultField("content")))
                .withPageable(pageable)
                .build();

        SearchHits<Hashtag> searchHits = elasticsearchOperations.search(query, Hashtag.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}
