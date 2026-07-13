package ar.com.paas.active.cluster.service.infrastructure.config;

import com.mongodb.ReadPreference;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion del cliente Mongo: pool de conexiones y read preference.
 */
@Configuration
public class MongoConfig {

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettingsCustomizer() {
        return builder -> builder
                .applyToConnectionPoolSettings(pool -> pool
                        .maxSize(100)
                        .minSize(10))
                .readPreference(ReadPreference.secondaryPreferred());
    }
}
