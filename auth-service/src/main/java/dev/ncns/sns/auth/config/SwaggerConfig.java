package dev.ncns.sns.auth.config;

import dev.ncns.sns.common.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@EnableOpenApi
@Configuration
public class SwaggerConfig {

    /**
     * 각 설정으로 구성된 Swagger API 문서를 생성합니다.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securitySchemes(List.of(apiKey()))
                .securityContexts(List.of(securityContext()))
                .useDefaultResponseMessages(false) // swagger에서 제공하는 defalult message를 제거합니다.
                .groupName("auth-server") // 각 서버를 구분하기 위한 이름 정의합니다.
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)) // RestContoller에 정의된 API만 등록합니다.
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API 문서 정보를 제공하기 위한 설정입니다.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("NCNS's API Documentation")
                .description("NCNS팀의 SNS 프로젝트 API 문서 : Auth-Service")
                .version("0.0.1")
                .build();
    }

    /**
     * Authorization Header 등록을 위한 설정입니다. (버튼 활성화)
     */
    private ApiKey apiKey() {
        return new ApiKey(Constants.AUTH_HEADER_KEY, "JWT", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return List.of(new SecurityReference(Constants.AUTH_HEADER_KEY, authorizationScopes));
    }

}
