package com.yesmarketing.ptsacs.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfigurationForTests {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<>();
        caches.add(credentialCacheBean().getObject());
        caches.add(subscriptionsCacheBean().getObject());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    @Qualifier("credential")
    public ConcurrentMapCacheFactoryBean credentialCacheBean() {
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("credential");
        return cacheFactoryBean;
    }

    @Bean
    @Qualifier("subscriptions")
    public ConcurrentMapCacheFactoryBean subscriptionsCacheBean() {
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("subscriptions");
        return cacheFactoryBean;
    }
}
