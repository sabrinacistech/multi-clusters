package ar.com.paas.cluster.status.monitor.infrastructure.config;

import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.WriteConcernResolver;

import java.time.Duration;

/**
 * Configuración de MongoDB: writeConcern majority y TTL de cache como bean.
 */
@Configuration
public class MongoConfig {

    /**
     * Fuerza writeConcern majority para todas las escrituras (durabilidad del WRITER).
     */
    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> WriteConcern.MAJORITY;
    }

    /**
     * TTL del cache derivado de la configuración.
     */
    @Bean(name = "cacheTtl")
    public Duration cacheTtl(CsmProperties properties) {
        return Duration.ofSeconds(properties.getCache().getTtlSeconds());
    }
}
