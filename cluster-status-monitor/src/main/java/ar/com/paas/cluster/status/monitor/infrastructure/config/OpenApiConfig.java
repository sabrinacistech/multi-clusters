package ar.com.paas.cluster.status.monitor.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos OpenAPI/Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI csmOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cluster Status Monitor API")
                        .description("PaaS Cluster Status Monitor (WRITER) - ABM y monitoreo de estado de clusteres")
                        .version("1.0.0")
                        .license(new License().name("Accenture PaaS")));
    }
}
