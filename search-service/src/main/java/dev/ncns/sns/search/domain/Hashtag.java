package dev.ncns.sns.search.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "hashtags")
public class Hashtag {

    private final Long id;

    private final String content;

    private final List<Long> postIdList;

    @PersistenceConstructor
    public Hashtag(Long id, String content, List<Long> postIdList) {
        this.id = id;
        this.content = content;
        this.postIdList = postIdList;
    }

}
