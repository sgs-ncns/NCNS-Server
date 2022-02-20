package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public void update(User user, String accountName, String nickname) {
        IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(User.class);
        Map<String, String> map = new HashMap<>();
        if (isDifferentName(user.getAccountName(), accountName)) {
            map.put("accountName", accountName);
        }
        if (isDifferentName(user.getNickname(), nickname)) {
            map.put("nickname", nickname);
        }
        Document document = Document.from(map);
        UpdateQuery updateQuery = UpdateQuery.builder(user.getId())
                .withDocument(document)
                .build();
        elasticsearchOperations.update(updateQuery, indexCoordinates);
    }

    private boolean isDifferentName(String name, String target) {
        return !name.equals(target);
    }

}
