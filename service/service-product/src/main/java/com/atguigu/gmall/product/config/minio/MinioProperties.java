package com.atguigu.gmall.product.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "app.minio")
@Component
public class MinioProperties {
    String endpoint;
    String ak;
    String sk;
    String bucketName;
}
