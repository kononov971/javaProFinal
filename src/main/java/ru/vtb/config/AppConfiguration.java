package ru.vtb.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.vtb.config.properties.RestTemplateProperties;
import ru.vtb.config.properties.TransactionsProperties;

@Configuration
@EnableConfigurationProperties(TransactionsProperties.class)
public class AppConfiguration {
    @Bean
    public RestTemplate restTemplate(TransactionsProperties properties) {
        RestTemplateProperties executorClient = properties.getTransactionsExecutorClient();
        return new RestTemplateBuilder()
                .rootUri(executorClient.getUrl())
                .setConnectTimeout(executorClient.getConnectionTimeout())
                .setReadTimeout(executorClient.getReadTimeout())
                .build();
    }
}
