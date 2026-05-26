package com.sabrinacistech.multiclusters;

import com.sabrinacistech.multiclusters.config.ClusterConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ClusterConfigProperties.class)
public class ClusterStatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusterStatusServiceApplication.class, args);
    }
}
