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
     * 클라이언트에서 AWS S3 를 이용해 이미지 썸네일화 및 저장을 한 뒤 해당 폴더 네임을 요청에 포함시켜 전송합니다.
     * 이후 클라이언트에게 이미지의 full path 를 넘겨주지 않아도 bucket/{userid}/{image_path} 에서 이미지를 가져올 수 있습니다.
     */
    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String imagePath;

    @Column(columnDefinition = "TEXT")
    private String content;

    /** 글에 포함된 해시태그는 ,로 concat 되어 저장됩니다. */
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