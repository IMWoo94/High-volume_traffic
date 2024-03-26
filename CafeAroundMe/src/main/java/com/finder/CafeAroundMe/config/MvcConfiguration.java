package com.finder.CafeAroundMe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[] {"classpath:/META-INF/resources/",
		"classpath:/resources/", "classpath:/static/", "classpath:/public/", "classpath:/templates/",
		"classpath:/map/"};

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/open-api/map/**")
			.addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}
}
