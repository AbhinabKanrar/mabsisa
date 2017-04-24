package com.mabsisa.portal.web.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class CorsFilter implements Filter {

	@Value("${cookie.csrfToken}")
	private String COOKIE_CSRF_TOKEN;
	

	public CorsFilter() {
		super();
	}
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		Cookie cookie = WebUtils.getCookie(httpServletRequest, COOKIE_CSRF_TOKEN);
		
		if (cookie == null || cookie.getValue() == null) {
			cookie = generateCookie();
			httpServletResponse.addCookie(cookie);
		}
		
		chain.doFilter(httpServletRequest, httpServletResponse);
		
	}

	@Override
	public void init(FilterConfig arg) throws ServletException {}
	
	private Cookie generateCookie() {
		Cookie cookie = new Cookie(COOKIE_CSRF_TOKEN, UUID.randomUUID().toString());
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		return cookie;
	}

}
