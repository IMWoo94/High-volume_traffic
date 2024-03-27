package com.finder.CafeAroundMe.api.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.finder.CafeAroundMe.api.common.annotation.LongLatLocation;
import com.finder.CafeAroundMe.api.domain.RequestLocation;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class LongLatLocationResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 지원하는 파라미터 정보 확인

		var annotation = parameter.hasParameterAnnotation(LongLatLocation.class);
		var parameterType = parameter.getParameterType().equals(RequestLocation.class);

		return (annotation && parameterType);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

		double longitude = Double.parseDouble(request.getParameter("longitude"));
		double latitude = Double.parseDouble(request.getParameter("latitude"));

		return new RequestLocation(longitude, latitude);
	}
}
