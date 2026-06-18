package com.sabrinacistech.multiclusters.config;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.*;
import org.springframework.cache.jcache.*;
import org.springframework.context.annotation.*;

import javax.cache.*;
import javax.cache.Caching;
import javax.cache.spi.*;
import com.sabrinacistech.multiclusters.model.*;
import java.time.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    @Value("${cluster-status.schedule-delay-seconds:60}")
    private long ttlSeconds;

    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager jCacheManager = provider.getCacheManager();

        javax.cache.configuration.Configuration<String, Cluster> config =
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                        String.class,
                                        Cluster.class,
                                        ResourcePoolsBuilder.heap(100)
                                ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ttlSeconds)))
                                .build()
                );

        jCacheManager.createCache("clusterStatusCache", config);

        return new JCacheCacheManager(jCacheManager);
    }
}