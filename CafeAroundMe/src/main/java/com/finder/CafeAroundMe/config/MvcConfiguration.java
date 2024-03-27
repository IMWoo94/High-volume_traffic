package com.finder.CafeAroundMe.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.finder.CafeAroundMe.api.common.resolver.LongLatLocationResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MvcConfiguration implements WebMvcConfigurer {

	private final LongLatLocationResolver longLatLocationResolver;

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[] {"classpath:/META-INF/resources/",
		"classpath:/resources/", "classpath:/static/", "classpath:/public/", "classpath:/templates/",
		"classpath:/map/"};

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/open-api/map/**")
			.addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(longLatLocationResolver);
	}
}
