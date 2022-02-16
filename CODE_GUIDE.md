# 👩‍💻 Code Guide 🧑‍💻

> SGS 프로젝트 리뷰를 위한 Code Guide <br>
> 가이드 작성자 : NCNS 팀 [@김연정](https://github.com/YJeongKim) <br>
> 코드 작성자 : NCNS 팀 Backend [@김연정](https://github.com/YJeongKim) & [@장유정](https://github.com/rachel5004)

### 워밍업

안녕하세요 :) <br>
저의 코드 가이드에 오신 것을 환영합니다! <br>
지금 보고 계신 브랜치는 `review-document-yeonjeong`으로, 여기로 돌아오는 길을 잃으셨을 때 참고해주세요. <br>
저희 팀과 프로젝트에 대한 전반적인 설명은 [README](https://github.com/sgs-ncns/NCNS-Server/blob/develop/README.md) 파일을 확인해주시면 감사하겠습니다. <br>
<br>
저희 백엔드 팀은 큰 서비스별로 역할을 나누기 보다는 서로의 코드를 이해하고 따라가기 위해 기능이나 이슈별로 역할을 나눠 진행하였습니다. <br>
물론 각자 작업한 부분은 나뉘었지만 문제에 대해 함께 고민하고 서로의 코드를 리뷰하며 작업하였기에 겹치는 부분이 있음을 미리 알려드립니다. <br>
그럼 재밌게 봐주시면 감사하겠습니다. 😊 <br>

### 역할

위에서 언급했듯이 저희 서버는 함께 기능을 추가하고 리팩토링하며 개발을 진행하였습니다. <br>
때문에 개인 담당 부분이라는 개념은 모호하지만, 가이드를 위해 베이스 코드 작성자 기준으로 임의로 분류한 역할은 다음과 같습니다. <br>

![role](https://user-images.githubusercontent.com/33328991/154035345-3ca5a8bd-d7a1-4aef-be65-011db3feca99.png)

### 목차
[1. MSA와 멀티 모듈](#MSA와-멀티-모듈) <br>
[2. Response Entity & Exception Handling](#Response-Entity-&-Exception-Handling) <br>
[3. JWT와 Redis를 이용한 인증](#JWT와-Redis를-이용한-인증) <br>

---

## MSA와 멀티 모듈

### MicroService Architecture 채택

#### 고민 사항과 해결 방법

평소 Monolithic Architecture로만 프로젝트를 구성하다 이번 기회에 MicroService Architecture로 개발을 진행하였습니다. <br>
이번 프로젝트는 실사용자를 고려한 대규모 프로젝트를 개발하는 것이었기 때문에 아래 이유로 Monolithic 구조로 구성할 수 없었습니다. <br>
```text
1. 한 서비스에서 장애가 발생하면 모든 프로젝트에 영향을 미칩니다.
2. 많은 사용자가 이용하고 있는 상황에서 서버가 잠깐이라도 멈추면 큰 문제가 발생합니다.
3. 어떤 로직에서 에러가 발생하면 관련 코드를 찾기 위해 모든 프로젝트를 찾아봐야 하는 경우가 발생합니다.
4. 한 서비스의 변경 사항을 적용하기 위해 모든 서비스가 함께 종료되어야 합니다.
```
이외에도 다양한 단점이 있겠지만, 이번 프로젝트는 Monolithic 구조의 단점을 해결할 수 있는 MicroService 구조로 진행하기로 하였습니다. <br>
```text
1. 한 서비스에서 장애가 발생하면 연관이 없는 다른 서비스에는 영향을 미치지 않습니다.
2. 한 서버가 멈추더라도 다른 서버는 정상 작동합니다.
3. 어떤 로직에서 에러가 발생하면 그 에러가 발생하는 프로젝트만 찾아보면 됩니다.
4. 한 서비스의 변경 사항을 적용하려면 그 서비스만 종료하였다가 재가동하면 됩니다.
```
물론 MSA가 마냥 장점만 있는 것은 아니지만, 이번 프로젝트 목표 "실사용자를 고려한 대규모 프로젝트"를 달성하기 위하여 채택하였습니다. <br>

### MSA 환경에서 멀티 모듈 구성

#### 고민 사항과 해결 방법

MSA로 프로젝트를 구성하다보니 공통으로 사용할 수 있는 중복 코드가 다수 있었습니다. <br>
`build.gradle`에 설정한 dependency나 응답 객체, Exception Handling에 대한 코드가 중복되어 공통으로 사용할 수 있도록 하고싶었습니다. <br>
따라서 기존 프로젝트를 멀티 모듈로 구성하기로 하였습니다. <br>
`settings.gradle`에 아래와 같이 root 프로젝트를 정의하고, 각 서비스를 모듈로 포함시켰습니다. <br>
```groovy
rootProject.name = 'ncns'
include('common-service')
include('gateway-service', 'eureka-service', 'config-service')
include('auth-service', 'user-service', 'post-service', 'feed-service')
```
그리고 root `build.gradle`에 공통으로 사용하는 의존성과 버전을 명시하였고, 각 모듈에서는 필요한 의존성을 추가하여 사용하였습니다. <br>
또한, 공통으로 사용하는 클래스에 대해 분리하기 위해 `common` 모듈을 생성하였고, 공통 로직이 필요한 클래스에서 해당 모듈을 import하여 사용하도록 하였습니다. <br>
이렇게 멀티 모듈을 구성함으로써 코드의 중복된 부분도 최소화할 수 있었으며, 프로젝트 배포시에도 각 모듈만 배포할 수 있어 수고를 덜 수 있었습니다. <br>

#### 해당 부분에 대한 코드

[`root settings.gradle`](./settings.gradle)
[`root build.gradle`](./build.gradle)
[`gateway build.gradle`](./gateway-service/build.gradle)
[`auth build.gradle`](./auth-service/build.gradle)
[`common module`](./common-service)
이하 생략

### Swagger 문서 통합

#### 고민 사항과 해결 방법

MSA로 프로젝트를 구성하다보니 Swagger를 이용한 API 문서가 각각 흩어져 있게 되었습니다. <br>
따라서 해당 서비스의 swagger를 접속하기 위해서 각 서버의 주소를 알고 있어야만 했으며, 매번 주소창을 변경하여 이동하여야 하였습니다. <br>
클라이언트 작업하는 팀원에게도 해당 문서 주소를 하나씩 전달하기 번거로웠고, 이를 불편하게 생각하여 Swagger 문서 통합을 하기로 하였습니다. <br>
<br>
Swagger version 3을 이용하여 API 문서를 필요로 하는 서버에 `SwaggerConfig`를 정의하였고, <br>
Gateway에서 `SwaggerGatewayFilter`를 정의하여 Swagger 문서 주소로 들어오는 요청을 받았으며, <br>
`SwaggerProvider`를 정의하여 들어온 요청에 대해 해당 서버의 Swagger 문서 URI를 만들어주었습니다. <br>
이렇게 Swagger 문서를 통합함으로써 API 문서 공유와 테스트를 쉽게 할 수 있었습니다. <br>

[배포된 Swagger 문서](http://15.165.120.145:9000/swagger-ui/index.html) (아직 배포하지 못한 서버의 문서는 확인할 수 없습니다.) <br>
<img src="https://user-images.githubusercontent.com/33328991/153801108-0c6d8b3b-1118-48c7-8e7a-dc79c6a193ca.png"> <br>
** 오른쪽 상단 drop down을 이용하여 각 서버 API 문서에 접근할 수 있습니다. <br>

#### 해당 부분에 대한 코드

[`SwaggerConfig`](./auth-service/src/main/java/dev/ncns/sns/auth/config/SwaggerConfig.java)
[`SwaggerGatewayFilter`](./gateway-service/src/main/java/dev/ncns/sns/gateway/config/filter/SwaggerGatewayFilter.java)
[`SwaggerProvider`](./gateway-service/src/main/java/dev/ncns/sns/gateway/util/SwaggerProvider.java)
이하 생략

<br>

## Response Entity & Exception Handling

### Response Entity

#### 고민 사항과 해결 방법

API 요청에 성공한 경우나 실패한 경우 모두에 대해서 클라이언트에게 아래와 같은 동일한 응답 형태로 응답을 내려주고 싶었습니다. <br>
```json
{
  "response_code": "0000/required/응답 코드",
  "message": "success :)/optional/결과에 대한 메시지",
  "data": {
    "" : "응답 데이터/optional/응답 객체"
  }
}
```
우리만의 응답을 내려주기 위해 공통 모듈에 `ResponseEntity`와 `ResponseType`을 정의해두고 필요한 각 모듈에서 import하여 사용하도록 하였습니다. <br>
또한, 응답 규칙을 세워 각 서버의 상황마다 다른 응답 코드를 주어 클라이언트에서 좀 더 세분화하게 응답을 다룰 수 있도록 하였습니다. <br>
```text
responseCode = 서버 port number 뒷자리 2자 + 응답 타입별 코드
서버 port number 뒷자리 : 00(auth), 10(user), 20(post), 30(feed), 99(gateway) ...
응답 타입별 코드 : 00 success, 01~99 fail
```
그리고 `successResponse()`, `failResponse()`를 다양하게 오버로딩하여 상황에 맞게 사용하였습니다. <br>

#### 해당 부분에 대한 코드

[`ResponseEntity`](./common-service/src/main/java/dev/ncns/sns/common/domain/ResponseEntity.java)
[`ResponseType`](./common-service/src/main/java/dev/ncns/sns/common/domain/ResponseType.java)

### Global Exception Handling

#### 고민 사항과 해결 방법

API 호출로 요청과 응답 과정에서 발생하는 모든 Exception에 대해서 일괄적으로 핸들링하고자 하였습니다. <br>
Exception이 발생하면 실패 응답으로 발생한 서버 정보와 응답 타입별 코드, error message를 응답으로 내려주어야 하는데, <br>
매번 try-catch 문으로 처리하고 싶지 않았고, 좀 더 우아한 방법으로 처리하고자 `ExceptionControllerAdvice`를 정의하였습니다. <br>
<br>
그리고 `RuntimeException`을 상속하는 Custom Exception `BusinessException`를 정의하였고, <br>
HttpStatus 상황별로 Exception을 핸들링하기 위해 `BusinessException`을 상속하는 `BadRequestException`, `UnauthorizedException` 등과 같은 Exception 클래스를 정의하였습니다. <br>
`ExceptionControllerAdvice`에서 각 Exception을 핸들링하는 method를 정의하여 실패 응답에 대해서도 동일한 응답 형태를 내려주면서 <br>
동시에 API 호출로 요청과 응답 과정에서 발생하는 모든 Exception을 핸들링 할 수 있게 되었습니다. <br>

#### 해당 부분에 대한 코드

[`ExceptionControllerAdvice`](./common-service/src/main/java/dev/ncns/sns/common/exception/ExceptionControllerAdvice.java)
[`BusinessException`](./common-service/src/main/java/dev/ncns/sns/common/exception/BusinessException.java)
[`BadRequestException`](./common-service/src/main/java/dev/ncns/sns/common/exception/BadRequestException.java)
[`UnauthorizedException`](./common-service/src/main/java/dev/ncns/sns/common/exception/UnauthorizedException.java)
[`NotFoundException`](./common-service/src/main/java/dev/ncns/sns/common/exception/NotFoundException.java)

### Gateway Exception Handling

#### 고민 사항과 해결 방법

Gateway Filter에서 JWT 검증과 파싱 작업을 함께 하고 있는데, 이 작업 중 Exception이 발생하면 우리만의 응답 형태로 응답할 수 없었습니다. <br>
또한 앞서 정의한 `ExceptionControllerAdvice`를 이용할 수도 없었습니다. <br>
그 이유는 먼저 common 모듈에 정의되어 있는 코드를 사용하기 위해 gateway에서 해당 모듈을 import 해야하는데, <br>
Spring Cloud(gateway)는 common 모듈에서 의존하고 있는 Spring Web과 호환이 되지 않습니다. <br>
따라서 gateway에서는 common 모듈을 이용할 수 없었고, `@ControllerAdvice`는 Contoller 단 안에서 발생하는 일들을 처리하기에 Filter 단까지 쓸 수 없었습니다. <br>
<br>
그래서 `ErrorWebExceptionHandler`를 구현하는 우리만의 `ExceptionHandler`를 정의하였습니다. <br>
`ExceptionControllerAdvice`와는 로직적인 면에서는 다르지만 최대한 유사한 형태로 handle() method를 정의하여 문제를 해결하였습니다. <br>

#### 해당 부분에 대한 코드

[`ResponseEntity`](./gateway-service/src/main/java/dev/ncns/sns/gateway/domain/ResponseEntity.java)
[`ResponseType`](./gateway-service/src/main/java/dev/ncns/sns/gateway/domain/ResponseType.java)
[`ExceptionHandler`](./gateway-service/src/main/java/dev/ncns/sns/gateway/exception/ExceptionHandler.java)
[`BusinessException`](./gateway-service/src/main/java/dev/ncns/sns/gateway/exception/BusinessException.java)
이하 생략

### FeignClinet 통신 중 발생한 Exception

#### 고민 사항과 해결 방법

FeignClient API 통신 중에 호출된 서버에서 발생한 Exception에 대해서 핸들링하고자 하였습니다. <br>
사례를 들자면, Auth -> User로 API 호출 중에 User Server에서 Exception이 발생하여 실패 응답이 전달되면 <br>
호출한 Auth Server에서는 기대하는 값을 전달받지 못하였기에 또 다른 Exception이 발생하게 되어 최종적으로 새로운 실패 응답이 전달되었습니다. <br>
<br>
호출된 서버에서 발생한 실패 응답을 그대로 전달해주되, 호출한 서버에서는 Exception을 핸들링 할 수 있도록 <br>
`ErrorDecoder`를 정의하고 FeignClient 설정에 등록해주어 문제를 해결하였습니다. <br>

#### 해당 부분에 대한 코드

[`FeignClientConfig`](./auth-service/src/main/java/dev/ncns/sns/auth/config/FeignClientConfig.java)
[`FeignErrorDecoder`](./auth-service/src/main/java/dev/ncns/sns/auth/util/FeignErrorDecoder.java)
[`UserFeignClient`](./auth-service/src/main/java/dev/ncns/sns/auth/controller/UserFeignClient.java)
[`FeignClientException`](./common-service/src/main/java/dev/ncns/sns/common/exception/FeignClientException.java)

<br>

## JWT와 Redis를 이용한 인증

### AccessToken과 RefreshToken 발급

#### 고민 사항과 해결 방법

발급한 사용자의 AccessToken과 RefreshToken 관리에 있어 여러 가지 고민을 하였습니다. <br>
AccessToken과 RefreshToken의 유효기간 부터해서, RefreshToken을 Server에서 어디서 어떻게 관리할지, <br>
사용자의 JWT 위조에 대해 어떻게 방지할지, 토큰이 만료되면 어떻게 처리할지 등 백엔드 팀원인 유정님과 함께 고민하였습니다. <br>
<br>
먼저 AccessToken은 30분, RefreshToken은 15일의 유효기간을 잡았습니다. <br>
그리고 AccessToken이 만료되면 RefreshToken을 검증하여 재발급을 하고, <br>
자동 로그인을 위해 재발급 중 RefreshToken 또한 기간이 1/3 미만이 되면 함께 재발급 해주도록 하였습니다. <br>
<br>
그리고 사용자의 RefreshToken에 대한 검증과 조작 방지를 위해 Redis와 Cookie를 함께 이용하였습니다. <br>
RefreshToken을 발급하면 Redis에 유효기간 만큼 저장하고, 클라이언트에 응답에 HttpOnly 속성으로 Cookie를 심어주었습니다. <br>
이렇게 함으로써 보안적으로 발생할 수 있는 CSRF나 XSS 문제를 방어할 수 있었습니다. (하지만, 스니핑에 대한 문제는 아직 미해결)<br>
<br>
AccessToken과 RefreshToken 발급에 대한 Flow는 다음과 같습니다. <br>
```text
1. 로그인 API를 요청합니다.
2. User 서버에서 사용자를 검증하고, Auth 서버에서 AccessToken과 RefreshToken을 발급합니다.
3. RefreshToken은 Redis와 Cookie에 저장하여 관리하고, AccessToken은 클라이언트에 저장하여 관리하여 사용합니다.
```
인증이 필요한 API 요청에 대한 Flow는 다음과 같습니다. <br>
```text
1. API를 요청합니다.
2. Gateway 서버에서 `Authorization` 헤더에 담긴 AccessToken을 검증하고, 만료되었다면 클라이언트에게 재발급 처리를 할 것을 응답합니다.
  a. Auth 서버에서 Cookie에 담긴 RefreshToken을 검증하고 재발급 해줍니다.
  b. RefreshToken이 만료되거나 잘못되었다면 클라이언트에게 로그아웃 처리를 할 것을 응답합니다.
3. Gateway 서버에서 검증이 완료되면 AccessToken을 파싱하여 payload에 담긴 userId를 `Authorized-User` 헤더에 담아 요청을 수행하도록 합니다.
```

#### 해당 부분에 대한 코드

[`JwtProvider`](./auth-service/src/main/java/dev/ncns/sns/auth/util/JwtProvider.java)
[`RedisManager`](./auth-service/src/main/java/dev/ncns/sns/auth/util/RedisManager.java)
[`CookieManager`](./auth-service/src/main/java/dev/ncns/sns/auth/util/CookieManager.java)
[`AuthService`](./auth-service/src/main/java/dev/ncns/sns/auth/service/AuthService.java)
[`AuthController`](./auth-service/src/main/java/dev/ncns/sns/auth/controller/AuthController.java)

### Gateway에서 AccessToken 검증과 파싱

#### 고민 사항과 해결 방법

Gateway에서 AccessToken을 검증하기 위해 `AuthorizationGatewayFilter` Filter를 정의하여 JWT 검증이 필요한 엔드포인트에 설정해주었습니다. <br>
또한, 각 서버에서 JWT 토큰을 파싱하는 공통 로직을 분리하기 위해 AccessToken 파싱을 Gateway에게 위임하였습니다. <br>
Gateway 서버에서 AccessToken을 파싱하여 payload에 담긴 userId를 `Authorized-User` 헤더에 담아 주었습니다. <br>
그리고 `AuthorizationInterceptor`를 정의하여 각 서버에서 `Authorized-User` 헤더에서 userId를 꺼내어 사용하도록 하였습니다. <br>
<br>
API 중 인증이 필요하지 않은 요청(회원가입 등)은 Custom Anotation인 `NonAuthorize`를 달아 통과하도록 하였습니다. <br>

#### 해당 부분에 대한 코드

[`AuthorizationGatewayFilter`](./gateway-service/src/main/java/dev/ncns/sns/gateway/config/filter/AuthorizationGatewayFilter.java)
[`gateway application.yml`](./gateway-service/src/main/resources/application.yml)
[`AuthorizationInterceptor`](./user-service/src/main/java/dev/ncns/sns/user/config/interceptor/AuthorizationInterceptor.java)
[`NonAuthorize`](./common-service/src/main/java/dev/ncns/sns/common/annotation/NonAuthorize.java)

<br>

### 마무리

DevCamp를 하면서 새롭게 도전한 것들과 다시 공부한 것들이 많습니다. <br>
MSA와 Spring Cloud, Redis 등과 같은 처음 접해본 기술들과 어디서 보고 주어온 코드로만 사용했었던 JWT, 멀티모듈, Security 등에 대해서도 <br>
내 것으로 만들기 위해 공부하고 코드로 구현해보면서 성취감을 느꼈습니다. <br>
<br>
DevCamp를 통해 평소에는 도전하지 못했던 영역에 도전하며, 현업에 가까운 많은 고민들을 할 수 있어 좋았습니다. <br>
구현에 급급하기보다 왜 이렇게 해야하는지 생각을 담아 코드 작성을 하였던 시간이었습니다. <br>
주어진 시간이 많지는 않지만, 남들이 보기 좋은 코드 작성을 위해 많은 고민도 하였습니다. <br>
이렇게 많은 고민을 하며 스스로가 개발자로 성장하고 있음을 몸소 느낄수 있었습니다. <br>
<br>
이번 코드 가이드를 작성하면서도 지금까지 고민했던 부분들을 되돌아 볼 수 있어 굉장히 유익했습니다. <br>
이 코드 가이드를 앞으로도 업데이트 하여 고민했던 것들에 대해 스스로가 정리할 수 있는 시간을 가져볼 것입니다. <br>
<br>
마지막으로 DevCamp라는 소중한 기회를 주셔서 감사드리며, 앞으로도 스스로 문제를 개척해 나가는 개발자가 되도록 노력하겠습니다.
<br>
지금까지 저의 코드 가이드를 읽어주셔서 감사합니다. :) <br>
<br>
-DevCamp 2기 Backend 김연정 작성- <br>
