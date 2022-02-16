## 🚩 코드 리뷰 가이드 

| [<img src="https://avatars.githubusercontent.com/u/75432228?v=4" width="100">](https://github.com/rachel5004) | 
|:---------------------------------------------------------------------------------------------:|
|                                [장유정](https://github.com/rachel5004)                                |

안녕하세요 :) SGS DevCamp 2기 NCNS 팀의 서버 개발을 맡은 장유정입니다!<br>
해당 문서는 코드 리뷰를 위한 간략한 가이드이며, 프로젝트에 대한 자세한 설명은 [README.md](https://github.com/sgs-ncns/NCNS-Server/blob/develop/README.md) 를 참고해주세요.<br>
이 문서에서는 담당한 기능과 중점적으로 고민해서 개발한 부분, 그 과정에서 느낀 점 등을 기술했습니다.<br>


1. [프로젝트 구조](#1-프로젝트-구조)
2. [개발 서버 및 기능](#2-개발-서버-및-기능)
3. [아래 코드를 중점적으로 봐주세요!](#3-아래-코드를-중점적으로-봐주세요)
4. [(appendix)  그 외의 고민들]((appendix)-그-외의-고민들)

## 1. 프로젝트 구조
  
``` sql
├── auth-service    # 인증 서버
├── common          # 공통 모듈
├── config-service  # 설정 서버
├── eureka-service  # service discovery
├── gateway-service # 게이트 웨이
├── post-service    # 게시글 서버
├── user-service    # 유저 서버
├── feed-service    # 피드(읽기 전용) 서버
|
├── setting.gradle
└── build.gradle

```
- 사용 기술
  - Java 11 + Spring Boot - 개발 언어 및 프레임워크
  - PostgreSQL - user 서버, post 서버 데이터 저장용
  - MongoDB - feed 서버 데이터 저장용
  - REDIS - 인증 정보 캐싱
  
 <img src="https://img.shields.io/badge/Java-11-007396?style=flat-square&logo=Java&logoColor=white"/></a>
 <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/></a>
 <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=PostgreSQL&logoColor=white"/></a>
 <img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white"/></a>
 <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/></a>

  
## 2. 개발 서버 및 기능


> 저희 서버는 서로의 코드를 이해하고 따라가기 위해 큰 서비스별로 역할을 나누지 않고, 서로의 기능을 추가하고 리팩토링하며 개발을 진행하였습니다.<br>
문제에 대해 함께 고민하고 서로의 코드를 리뷰하며 작업하였기 때문에 개인 담당 부분이라는 개념은 모호하지만, 가이드를 위해 베이스 코드 작성자 기준으로 임의로 분류한 역할임을 미리 알려드립니다.<br>



![role](https://user-images.githubusercontent.com/75432228/154035377-30698cb2-8ee4-47f2-a62b-cc6f976bb754.png)



  - **Config 서버**
    - Spring Cloud 에서 사용되는 설정 관리를 서버입니다.
    - discovery client 등 모든 서비스에서 중복되는 설정을 중앙화했습니다.
    - 배포 환경별 DB 설정 관리합니다.
    - spring actuator를 이용해 실시간 변경 사항 반영합니다.
    - Spring Cloud Config 에서 제공하는 Encrypt/Decrypt 를 통해 원격 저장소에 올라가는 민감 정보를 암호화합니다.
    - 서버에서 사용되는 암호화 키와 저장소 계정 정보 등은 Jasypt 라이브러리를 이용해 암호화되었습니다.
    
  - **User 서버**
    - 회원 가입 / 회원 탈퇴 요청을 처리합니다.
    - 유저 프로필 조회 및 수정, 팔로워 / 팔로잉 조회 요청을 처리합니다.
    - 팔로우 요청은 데이터 상태를 확인 후 토글합니다.
    - (Auth Feign API) 로그인 타입 별 로직 처리 및 리스폰스를 반환합니다.
    - (Post Feign API) 게시글 작성/삭제 시 count 데이터를 Syncronize 합니다.
    
  - **Post 서버**
    - 게시글 작성, 수정, 삭제 요청을 처리합니다.
    - 댓글 작성, 수정, 삭제 요청을 처리합니다.
    - 게시글 좋아요 요청은 데이터 상태를 확인 후 토글합니다.
    - 검색 서버에서 사용될 해시 태그 데이터를 핸들링합니다.
    - 내가 태그된 게시물 조회에서 사용될 유저 태그 데이터를 핸들링합니다.
    - (Feed Feign API) 피드를 요청한 유저와 피드가 마지막으로 업데이트 된 시간을 기반으로 누적된 신규 게시글을 피드 형태로 반환합니다.
    
  - **Feed 서버**
    - CQRS 정책을 적용한 읽기 전용 서버입니다.
    - 서비스 요구사항에 따라 Pull / Push 정책을 나누어 구현했습니다.
    - 서버간 통신 개선을 위해 Push 정책은 이벤트 소싱 방식으로 전환 중입니다.
    - (User Feign API) 팔로잉, 구독 정보를 업데이트합니다.
    - (Post Feign API) 게시글 작성, 삭제 시 구독자들의 피드를 업데이트합니다.
    


## 3. 아래 코드를 중점적으로 봐주세요
#### 1) Multi Module
저희 프로젝트는 멀티 모듈 구조로 구성되어있습니다.<br>
`common module` 생성 및 주입으로 코드 중복을 최소화하고, 루트에서 빌드를 간편하게 할 수 있도록 했습니다. <br>
common 모듈에는 전역적으로 사용되는 `ResponseEntity`, `Exception Handler`, `Authorize annotaion` 등이 포함되었습니다.<br><br>
<관련 코드>

  [build.gradle](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/build.gradle)<br>
  [settings.gradle](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/settings.gradle) <br>
 [common-module/ResponseEntity](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/common-service/src/main/java/dev/ncns/sns/common/domain/ResponseEntity.java) <br>
 [common-module/ExceptionControllerAdvice](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/common-service/src/main/java/dev/ncns/sns/common/exception/ExceptionControllerAdvice.java)
 
#### 2) Authorized User
대부분의 CUD 요청은 게이트웨이에서 토큰을 파싱해 헤더에 담아둔 인증 정보를 기반으로 권한을 검증합니다.<br>
요청 시 `Interceptor` 에서 Authorize 정보를 확인해 `security context`에 저장합니다.<br>
인증 정보가 필요하지 않은 요청은 `@NonAuthorize` 어노테이션을 통해 제외할 수 있습니다.<br>
서버간 통신이나 회원가입 등 인증정보를 필요로 하지 않는 특정 요청들이 있고, 클라이언트 요청과 관련된 공통 작업이기 때문에 `filter`가 아닌 `Interceptor`를 선택했습니다.

<관련 코드>

 [AuthorizationInterceptor](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/user-service/src/main/java/dev/ncns/sns/user/config/interceptor/AuthorizationInterceptor.java)
 
#### 3) ERD 설계
댓글, 좋아요, 팔로우/팔로워 등 많은 row를 가질 확률이 높은 테이블들이 있습니다.<br>
SNS 서비스는 read 요청이 빈번하기 때문에 count 쿼리의 부담이 크고, 이를 해결하기 위해 `count table`을 생성했습니다.<br>
해시 태그는 게시글 조회 요청에서는 일반 텍스트로 취급되고, on change 로 요청이 잦은 검색 서비스에서 관련 게시글 수를 빠르게 보여줘야 합니다.<br>
또한 검색 결과 확인 시 `DB io(select where)` 연산보다 `Java String` 연산의 비용이 적다는 점을 고려해 Post 테이블의 `hashtag column`과 content와 count 를 메인으로 하는 `Hashtag 테이블`을 따로 설계했습니다.

<관련 코드>

[UserCountRepository](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/user-service/src/main/java/dev/ncns/sns/user/repository/UserCountRepository.java)<br>
  [post entity](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/post-service/src/main/java/com/ncns/sns/post/domain/Post.java)  <br>
  [hashtag entity](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/post-service/src/main/java/com/ncns/sns/post/domain/Hashtag.java) <br>
  [postservice](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/post-service/src/main/java/com/ncns/sns/post/service/PostService.java) <br>

#### 4) `CQRS(Command and Query Responsibility Segregation)` 정책 적용
위에서 언급했듯 sns 서비스의 특성상 read 요청이 매우 많습니다. 때문에 MSA 구조의 장점을 살려 CUD(Command)와 R(Query)의 책임을 분리하는 CQRS 패턴을 적용했습니다.<br>
읽기 전용인 피드 서버는 `Document 기반의 DB 인 MongoDB` 를 선택했습니다.<br>
1차 개발 단계인 현재에서는 feign 요청으로 syncronize를 맞추고있지만, 추후 캐싱과 메세지 큐를 통한 이벤트 소싱을 적용해 개선할 예정입니다.<br>
`Pull 정책`은 피드 조회 요청 시 Post 서버에 Syncronize 를 요청합니다. 누적된 신규 피드를 한번에 가져오기 때문에 통신 횟수가 적다는 장점이 있지만, 장애 시 누락되는 정보가 많을 위험이 있습니다.<br>
`Push 정책`은 게시글 작성 시 Feed 서버로 Syncronize 를 요청합니다. 게시글 작성 요청마다 통신하기 때문에 통신 비용이 크지만, Post 서버에 장애가 생겨도 어느 정도의 Syncronize를 보장할 수 있습니다.<br>
"구독" 기능은 일반 팔로잉과 다르게 어떤 상황에서도 가장 빠른 전달을 목표로 하기 때문에, 장애 상황에서도 구독한 유저의 게시글만큼은 보장할 수 있도록 두 정책을 선택적으로 적용했습니다.<br>
 
 <관련 코드>
 
 [FeedController](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/feed-service/src/main/java/dev/ncns/sns/feed/controller/FeedController.java) <br>
 [Post/FeedFeignService](https://github.com/sgs-ncns/NCNS-Server/blob/review-document-yoojeong/post-service/src/main/java/com/ncns/sns/post/service/FeedFeignService.java)
 
 ## (appendix) 그 외의 고민들
 
프로젝트를 진행하고, 리팩토링과 클린코드에 대한 고민을 많이 하게 되었습니다.<br><br>
로직 하나를 수정하면 관련해 수정할 코드들이 생겨나는 것을 보며 SOLID 한 설계의 중요성을 많이 느끼게 되었습니다.<br><br>
더불어 로직을 수정해도 적용할 수 있는 테스트 코드에 대해 생각하게 되었습니다.<br>
단순히 현재 작성한 서비스 코드가 예상대로 작동하는지가 아닌, 기본적인 것들은 코드 재작성 없이 테스트를할 수 있도록 basic 하고 reusable 한 테스트코드를 작성해야겠다고 느꼈습니다.<br><br>
팀원과 코드 리뷰를 진행하며 코드 리뷰의 목적에 대해서도 고찰하게 되었습니다.<br>
비즈니스 로직과 분리되지않은 컨트롤러 등 각자의 역할이 모호한 코드, 무분별한 exception 등 잘못된 점을 개선하는 것 뿐만 아니라 적절한 네이밍과 컨벤션 준수 확인 등 서로가 좋은 코드를 짜기 위해 상호보완하는 과정이라는 것을 느꼈습니다.<br>
또한  코드 메이커가 놓친 에러 시나리오를 캐치하고 현재보다 좋을 것 같은 로직을 제안하며 성장까지도 도모할 수 있는 개발자가 되고싶다고 생각하는 계기가 되었습니다.<br>

DevCamp 라는 좋은 기회를 얻어 3개월동안 정말 많은 것이 바뀌었습니다.<br>
새로운 기술과 아키텍쳐에 설레며 도전할 자신감을 얻고, 좋은 코드와 좋은 개발자란 무엇인지 끊임 없이 고찰하게 되었습니다.<br>
이전의 제 자신도 많이 노력했다고 생각함에도, 이 경험이 제 커리어에 터닝 포인트가 됐다고 말할 수 있을 만큼 제 시야를 넓혀주었습니다.<br>
좋은 기회를 주신 것에 정말 감사드리고, 앞으로도 좋은 모습을 보여드릴 수 있도록 정진하겠습니다 :)<br><br>

시간 내어 읽어주셔서 감사합니다. 이상 DevCamp 2기 장유정이었습니다!
