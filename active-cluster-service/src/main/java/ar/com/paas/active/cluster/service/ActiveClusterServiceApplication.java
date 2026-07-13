package ar.com.paas.active.cluster.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Punto de entrada del microservicio active-cluster-service (READER).
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo")
public class ActiveClusterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiveClusterServiceApplication.class, args);
    }
}
