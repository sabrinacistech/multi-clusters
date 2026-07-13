package ar.com.paas.cluster.status.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Punto de entrada del microservicio Cluster Status Monitor (WRITER).
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
@ConfigurationPropertiesScan
public class ClusterStatusMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusterStatusMonitorApplication.class, args);
    }
}
