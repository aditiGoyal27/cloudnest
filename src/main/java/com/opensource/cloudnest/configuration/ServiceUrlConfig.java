package com.opensource.cloudnest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Configuration
@PropertySource("classpath:url.properties")
public class ServiceUrlConfig {
    @Value("${base.url}")
    private String service1BaseUrl;

    @Value("${reset.password}")
    private String resetLink;

    public String getResetLinkUrl() {
        return service1BaseUrl + resetLink;
    }



}
