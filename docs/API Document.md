# API 문서

## 요청 헤더

```java
{
    "Authorization": Bearer {token}
}
```

## 응답 형태

```java
{
    "response_code" : string,    // required, 임의의 코드
    "message"       : string,    // optional, 상태에 대한 메시지
    "data"          : object     // optional, 응답 객체
}
```

## 응답코드

```java
서버no + 상태

// server number
00(auth)
10(user)
20(post)
30(feed)
40(search)
99(gateway)
// state
00 success
01~99 fail
```

## 응답 유형

```
    SUCCESS("00", "success :)"),
    FAILURE("99", "failure :("), // 처리되지 않은 서버 에러

    // COMMON
    ARGUMENT_NOT_VALID("01", "Argument 유효성 검증에 실패하였습니다."),
    REQUEST_NOT_VALID("02", "유효하지 않는 요청입니다."),
    REQUEST_UNAUTHORIZED("03", "비인증된 요청입니다."),
    JWT_NOT_VALID("04", "토큰 검증에 실패하였습니다."),
    JWT_MALFORMED("05", "위조된 토큰입니다."),
    JWT_UNSUPPORTED("06", "지원하지 않는 토큰입니다."),
    JWT_SIGNATURE("07", "시그니처 검증에 실패한 토큰입니다."),
    JWT_EXPIRED("08", "만료된 토큰입니다."),
    JWT_NULL_OR_EMPTY("09", "토큰이 없거나 값이 비어있습니다."),
    JWT_HEADER_PREFIX("10", "토큰 값은 'Bearer' 로 시작해야 합니다."),

    // AUTH
    AUTH_NULL_TOKEN("11", "토큰을 찾을 수 없습니다."),
    AUTH_NOT_SAME_TOKEN("12", "저장된 토큰과 일치하지 않습니다."),
    AUTH_NOT_SAME_USER("13", "Access와 Refresh 토큰의 사용자가 일치하지 않습니다."),
    AUTH_NOT_FOUND_REDIS_KEY("14", "Redis에서 해당 Key를 찾을 수 없습니다."),
    AUTH_NOT_FOUND_COOKIE_KEY("15", "Cookie에서 해당 Key를 찾을 수 없습니다."),

    // USER
    USER_NOT_EXIST_ID("11", "존재하지 않은 사용자 ID입니다."),
    USER_NOT_EXIST_EMAIL("12", "존재하지 않은 이메일입니다."),
    USER_NOT_EXIST_ACCOUNT_NAME("13", "존재하지 않은 계정 이름입니다."),
    USER_DUPLICATED_EMAIL("14", "중복된 이메일입니다."),
    USER_DUPLICATED_ACCOUNT_NAME("15", "중복된 계정 이름입니다."),
    USER_NOT_MATCH_PASSWORD("16", "비밀번호가 일치하지 않습니다."),
    USER_NOT_MATCH_AUTH_TYPE("17", "가입 인증 유형이 일치하지 않습니다."),

    // POST
    POST_NOT_EXIST("11", "존재하지 않은 게시글입니다."),
    POST_NOT_EXIST_HASHTAG("12", "존재하지 않은 해시태그입니다."),
    POST_NOT_EXIST_COMMENT("13", "존재하지 않은 댓글입니다."),
    POST_NOT_AUTHOR("14", "작성자가 아닙니다."),

    // SEARCH
    SEARCH_NOT_EXIST_USER("11", "존재하지 않은 사용자입니다."),
    SEARCH_NOT_EXIST_HASHTAG("12", "존재하지 않은 해시태그입니다."),

```

## S3

|                  |                                                                                                                                                                                                       |
| ---------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| s3 주소          | https://sgsncns130837-dev.s3.ap-northeast-2.amazonaws.com/public/{유저명}/{시간}/사진.png                                                                                                             |
| s3 테스트 파일들 | https://sgsncns130837-dev.s3.ap-northeast-2.amazonaws.com/public/test1/{11.png, 45.png, 169.png} <br>https://sgsncns130837-dev.s3.ap-northeast-2.amazonaws.com/public/test2/{mascot_00.jpg, NCNS.png} |
|                  |                                                                                                                                                                                                       |

## AUTH

| 기능               |                                  method                                   | endpoint    |
| :----------------- | :-----------------------------------------------------------------------: | :---------- |
| AccessToken 재발급 |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/auth` |
| 로그아웃           | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/auth` |
| 계정 이름 로그인   |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/auth` |
| 자체 로그인        |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/auth` |
| 소셜 로그인        |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/auth` |
|                   |                                                                          |              |

## USER

| 기능        |                                  method                                   | endpoint                          |
| :---------- | :-----------------------------------------------------------------------: | :-------------------------------- |
| 회원가입    |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/user `                      |
| 회원탈퇴    | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/user `                      |
| 프로필 수정 | <img src="https://img.shields.io/badge/PATCH-FFBB00?style=flat-square"/>  | `/api/user `                      |
| 프로필 조회 |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/user/{account_name} `       |
| 팔로워 조회 |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/user/{user_id}/followers `  |
| 팔로잉 조회 |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/user/{user_id}/following `  |
| 팔로우 신청 |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/user/follow/{target_id} `   |
| 낀부 조회   |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/user/subscribing `          |
| 깐부 신청   |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/user/subscribe/{target_id}` |
|             |

| SERVER ONLY         |                                                                         |                         |
| :------------------ | ----------------------------------------------------------------------- | ----------------------- |
| 이메일 중복 체크    | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/> | `/api/user/email `      |
| 계정 이름 중복 체크 | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/> | `/api/user/account `    |
| 로그인 정보 체크    | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/> | `/api/user/login `      |
| 게시글 수 수정      | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/> | `/api/user/count/post ` |
| 마지막 접속         | <img src="https://img.shields.io/badge/PUT-BE95FF?style=flat-square"/>  | `/api/user/access `     |
|                     |                                                                         |                         |

## POST

| 기능                    |                                  method                                   | endpoint                          |
| :---------------------- | :-----------------------------------------------------------------------: | :-------------------------------- |
| 게시물 작성             |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/post`                       |
| 게시물 수정             | <img src="https://img.shields.io/badge/PATCH-FFBB00?style=flat-square"/>  | `/api/post`                       |
| 게시물 삭제             | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/post/{post_id} `            |
| 사용자 게시물 목록 조회 |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/post?userId={user_id} `     |
| 게시물 상세보기         |  <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>   | `/api/post/{post_id} `            |
| 좋아요                  |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/post/like/{post_id} `       |
| 댓글 작성               |  <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>  | `/api/post/comment `              |
| 댓글 수정               | <img src="https://img.shields.io/badge/PATCH-FFBB00?style=flat-square"/>  | `/api/post/comment `              |
| 댓글 삭제               | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/post/comment/{comment_id} ` |
|                        |                                                                           |                                  |

| SERVER ONLY           |                                                                         |                      |
| :-------------------- | ----------------------------------------------------------------------- | -------------------- |
| 피드 동기화           | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/> | `/api/post/feed `    |
| 게시글 요약 목록 조회 | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/> | `/api/post/summary ` |
|                       |                                                                         |                      |

## FEED

| 기능           |                                 method                                 | endpoint                |
| :------------- | :--------------------------------------------------------------------: | :---------------------- |
| 일반 피드 조회 | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/> | `/api/feed?page={page}` |
| 깐부 피드 조회 | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/> | `/api/feed/subscribing` |
|                |

| SERVER ONLY        |                                                                        |                  |
| :----------------- | ---------------------------------------------------------------------- | ---------------- |
| 전체 피드정보 조회 | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/> | `/api/feed/all ` |
|                    |                                                                        |                  |

## SEARCH

| 기능          |                                 method                                 | endpoint                                      |
| :------------ | :--------------------------------------------------------------------: | :-------------------------------------------- |
| 전체 검색     | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/> | `/api/search/type/all?keyword={keyword}`      |
| 사용자 검색   | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/> | `/api/search/type/user?keyword={keyword}`     |
| 해시태그 검색 | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/> | `/api/search/type/hashtag?keyword={keyword} ` |
|               |                                                                        |                                               |

| SERVER ONLY               |                                                                           |                                                    |
| :------------------------ | ------------------------------------------------------------------------- | -------------------------------------------------- |
| 사용자 조회               | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>    | `/api/search/user/account-name?keyword={keyword} ` |
| 계정 이름으로 사용자 조회 | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>    | `/api/search/user/{user_id} `                      |
| 닉네임으로 사용자 조회    | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>    | `/api/search/user/{user_id} `                      |
| 사용자 등록               | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>   | `/api/search/user `                                |
| 사용자 수정               | <img src="https://img.shields.io/badge/PATCH-FFBB00?style=flat-square"/>  | `/api/search/user `                                |
| 사용자 삭제               | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/search/user/{user_id} `                      |
| 해시태그 조회             | <img src="https://img.shields.io/badge/GET-11B48A?style=flat-square"/>    | `/api/search/hashtag?content={content}} `          |
| 해시태그 등록             | <img src="https://img.shields.io/badge/POST-2490D7?style=flat-square"/>   | `/api/search/hashtag `                             |
| 해시태그 수정             | <img src="https://img.shields.io/badge/PATCH-FFBB00?style=flat-square"/>  | `/api/search/hashtag `                             |
| 해시태그 삭제             | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/search/hashtag?content={content} `           |
| 내용으로 해시태그 조회    | <img src="https://img.shields.io/badge/DELETE-d14836?style=flat-square"/> | `/api/search/hashtag/content?keyword={keyword} `   |
|                           |                                                                           |                                                    |
