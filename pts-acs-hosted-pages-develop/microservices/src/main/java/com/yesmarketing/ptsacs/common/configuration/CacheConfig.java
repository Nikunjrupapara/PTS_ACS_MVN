package com.yesmarketing.ptsacs.common.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@ConfigurationProperties(prefix="caching")
public class CacheConfig {
	public CacheConfig() {
	}

	public static class CacheSpec {
		private Integer timeout = 120;
		private Integer max = 200;

		public CacheSpec () {

		}

		public Integer getTimeout() {
			return timeout;
		}

		public void setTimeout(Integer timeout) {
			this.timeout = timeout;
		}

		public Integer getMax() {
			return max;
		}

		public void setMax(Integer max) {
			this.max = max;
		}
	}

	private Map<String, CacheSpec> specs;


	@Bean
	public CacheManager cacheManager(Ticker ticker) {
		SimpleCacheManager manager = new SimpleCacheManager();

		if (specs != null) {
			List<CaffeineCache> caches = specs.entrySet().stream().map (
				entry->buildCache(entry.getKey(), entry.getValue(), ticker)).collect(Collectors.toList());
			manager.setCaches(caches);
		}
		return manager;
	}


	private CaffeineCache buildCache (String name, CacheSpec cacheSpec, Ticker ticker) {
		LOG.info("Cache {} specified timeout of {} seconds, max of {}",name,cacheSpec.timeout,cacheSpec.max);

		final Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
															 .expireAfterWrite(cacheSpec.timeout, TimeUnit.SECONDS)
															 .maximumSize(cacheSpec.max)
															 .ticker(ticker);
		return new CaffeineCache(name, caffeineBuilder.build());
	}
	@Bean
	public Ticker ticker() {
		return Ticker.systemTicker();
	}


	public Map<String, CacheSpec> getSpecs() {
		return specs;
	}


	public void setSpecs(Map<String, CacheSpec> specs) {
		this.specs = specs;
	}
}
