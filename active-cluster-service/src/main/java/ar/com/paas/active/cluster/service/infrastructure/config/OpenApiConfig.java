package ar.com.paas.active.cluster.service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de la documentacion OpenAPI (springdoc).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI activeClusterServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("active-cluster-service")
                .description("PaaS Active Cluster Service (READER)")
                .version("1.0.0-SNAPSHOT"));
    }
}
