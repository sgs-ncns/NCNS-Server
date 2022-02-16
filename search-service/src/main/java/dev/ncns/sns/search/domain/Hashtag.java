package dev.ncns.sns.search.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

@Getter
@Setting(settingPath = "settings.json")
@Document(indexName = "hashtags")
public class Hashtag {

    @Id
    private String id;

    @Field
    private final String content;

    @Field
    private final List<Long> postIdList;

    @Builder
    @PersistenceConstructor
    public Hashtag(String content, List<Long> postIdList) {
        this.content = content;
        this.postIdList = postIdList;
    }

}
