package ru.vtb.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations.executors")
public class TransactionsProperties {
    private final RestTemplateProperties transactionsExecutorClient;

    public TransactionsProperties(RestTemplateProperties transactionsExecutorClient) {
        this.transactionsExecutorClient = transactionsExecutorClient;
    }
}
