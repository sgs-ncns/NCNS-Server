package dev.ncns.sns.search.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "users")
public class User {

    private final Long id;

    private String accountName;

    private String nickname;

    @PersistenceConstructor
    public User(Long id, String accountName, String nickname) {
        this.id = id;
        this.accountName = accountName;
        this.nickname = nickname;
    }

}
