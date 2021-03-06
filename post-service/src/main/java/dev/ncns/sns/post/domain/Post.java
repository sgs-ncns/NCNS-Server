package dev.ncns.sns.post.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update posts set deleted_at=now() where id=?")
@Where(clause = "deleted_at is null")
@Table(name = "posts")
@Entity
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String imagePath;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String hashtag;

    @Builder
    public Post(Long userId, String accountName, String content, String imagePath, String hashtag) {
        this.userId = userId;
        this.accountName = accountName;
        this.content = content;
        this.imagePath = imagePath;
        this.hashtag = hashtag;
    }

    public Post updatePost(String content, String hashtag) {
        this.content = content;
        this.hashtag = hashtag;
        return this;
    }

    public List<String> getHashtagList() {
        return Arrays.asList(this.hashtag.split(","));
    }

}