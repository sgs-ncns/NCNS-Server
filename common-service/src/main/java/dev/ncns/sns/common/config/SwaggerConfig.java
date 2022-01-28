package dev.ncns.sns.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE = "Bearer {accessToken}";

    @Bean
    public Docket api() {
        RequestParameterBuilder requestParameterBuilder = new RequestParameterBuilder();
        requestParameterBuilder.name(AUTH_HEADER_KEY)
                .required(false)
                .in(ParameterType.HEADER)
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(List.of(apiKey()))
                .useDefaultResponseMessages(false)
                .globalRequestParameters(List.of(requestParameterBuilder.build()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Arrays.asList(new SecurityReference(AUTH_HEADER_VALUE, authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("NCNS's SNS API Document!")
                .description("SNS팀의 SNS 프로젝트 API 문서")
                .version("0.0.1")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTH_HEADER_VALUE, AUTH_HEADER_KEY, "header");
    }

}
