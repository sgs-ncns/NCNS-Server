package dev.ncns.sns.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private String port;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration;
        if (profile.equals("prod")) {
            clientConfiguration = getClientConfigurationWithProd();
        } else {
            clientConfiguration = getClientConfigurationWithLocal();
        }
        return RestClients.create(clientConfiguration).rest();
    }

    private ClientConfiguration getClientConfigurationWithProd() {
        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .usingSsl()
                .build();
    }

    private ClientConfiguration getClientConfigurationWithLocal() {
        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
    }

}
