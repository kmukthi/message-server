package com.sweagle.messageserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

import com.sweagle.messageserver.entity.CachedData;

@Configuration
public class ScopesConfig {

	@Bean
	@ApplicationScope
    public CachedData applicationScopedBean() {
        return new CachedData();
    }
}
