package dev.ncns.sns.search.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setting(settingPath = "settings.json")
@Document(indexName = "users")
public class User {

    @Id
    private String id;

    @Field
    private final Long userId;

    @Field
    private final String accountName;

    @Field
    private final String nickname;

    @Builder
    @PersistenceConstructor
    public User(Long userId, String accountName, String nickname) {
        this.userId = userId;
        this.accountName = accountName;
        this.nickname = nickname;
    }

}
