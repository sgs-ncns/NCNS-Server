package dev.ncns.sns.search.repository;

import dev.ncns.sns.search.domain.Hashtag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagQueryRepository {

    void update(Hashtag hashtag, List<Long> postIdList);

}
