package ru.vtb.config.properties;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RestTemplateProperties {
    private String url;
    private Duration connectionTimeout;
    private Duration readTimeout;
}
