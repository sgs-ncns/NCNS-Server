package dev.ncns.sns.search.service.kafka;

import dev.ncns.sns.search.service.HashtagService;
import dev.ncns.sns.search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchConsumerService {

    private final UserService userService;
    private final HashtagService hashtagService;

}
