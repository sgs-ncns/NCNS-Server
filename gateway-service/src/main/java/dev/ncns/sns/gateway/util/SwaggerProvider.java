package dev.ncns.sns.gateway.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "open-api.swagger")
@EnableConfigurationProperties
@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {

    @Getter
    @Setter
    private List<String> resources;

    public static final String API_URI = "/v3/api-docs";

    /**
     * path를 분리하여 Swagger URI를 만듭니다.
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> swaggerResources = new ArrayList<>();
        resources.forEach(s -> {
            String[] split = s.split(",");
            swaggerResources.add(resource(split[0], split[1] + API_URI, split[2]));
        });
        return swaggerResources;
    }

    /**
     * 각 서버 Swagger Config에 설정한 Group 이름으로 쿼리 파라미터를 구성합니다.
     */
    private SwaggerResource resource(String name, String oas3Url, String swaggerGroup) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);

        if (Docket.DEFAULT_GROUP_NAME.equals(swaggerGroup)) {
            swaggerResource.setUrl(oas3Url);
        } else {
            swaggerResource.setUrl(oas3Url + "?group=" + swaggerGroup);
        }

        swaggerResource.setSwaggerVersion("3.0.3");
        return swaggerResource;
    }

}
