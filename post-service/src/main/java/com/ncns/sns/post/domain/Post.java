package com.ncns.sns.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;


@Getter
@ToString
@NoArgsConstructor
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

    /**
     * s3 이미지 주소입니다.
     * 클라이언트에서 AWS S3 를 이용해 이미지 썸네일화 및 저장을 한 뒤 해당 폴더 네임을 요
     */
    @Column(nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String content;

    /** 글에 포함된 해시태그는 ,로 concat 되어 저장됩니다. */
    @Column
    private String hashtag;

    @Builder
    public Post(Long userId, String content, String image, String hashtag) {
        this.userId = userId;
        this.content = content;
        this.image = image;
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