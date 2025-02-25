package com.danko.danko_handmade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudflare.r2")
@Data
public class CloudflareR2Config {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

}
