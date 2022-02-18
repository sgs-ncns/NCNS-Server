package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class HashtagQueryRepositoryImpl implements HashtagQueryRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void update(Hashtag hashtag, List<Long> postIdList) {
        IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(Hashtag.class);
        Map<String, List<Long>> map = new HashMap<>();
        map.put("postIdList", postIdList);
        Document document = Document.from(map);
        UpdateQuery updateQuery = UpdateQuery.builder(hashtag.getId())
                .withDocument(document)
                .build();
        elasticsearchOperations.update(updateQuery, indexCoordinates);
    }

}
