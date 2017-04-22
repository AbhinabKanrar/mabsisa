package com.mabsisa.portal.web.filter;

import java.io.IOException;

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

import com.mabsisa.portal.common.util.SecurityUtil;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class SecurityFilter implements Filter {

	@Value("${cookie.accessToken}")
	private String COOKIE_ACCESS_TOKEN;

	@Value("${url.login}")
	private String URL_LOGIN;

	@Value("${url.logout}")
	private String URL_LOGOUT;

	private SecurityFilter() {
		super();
	}

	@Override
	public void init(FilterConfig arg) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		Cookie cookie = WebUtils.getCookie(httpServletRequest, COOKIE_ACCESS_TOKEN);

		if (httpServletRequest.getRequestURI().equalsIgnoreCase(URL_LOGIN)) {
			if (cookie == null) {
				chain.doFilter(httpServletRequest, httpServletResponse);
			} else if (SecurityUtil.getClaims(cookie.getValue()) == null) {
				removeInstance(cookie, httpServletResponse);
			}
		} else if (httpServletRequest.getRequestURI().equalsIgnoreCase(URL_LOGOUT)) {
			removeInstance(cookie, httpServletResponse);
		} else if (cookie == null) {
			redirectToLogin(httpServletResponse);
		} else {
			if (SecurityUtil.getClaims(cookie.getValue()) == null) {
				removeInstance(cookie, httpServletResponse);
			} else {
				chain.doFilter(httpServletRequest, httpServletResponse);
			}
		}

	}

	@Override
	public void destroy() {}

	private static void destroyCookie(Cookie cookie) {
		cookie.setPath("/");
		cookie.setMaxAge(0);
	}

	private void redirectToLogin(HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.sendRedirect(URL_LOGIN);
	}

	private void removeInstance(Cookie cookie, HttpServletResponse httpServletResponse) throws IOException {
		destroyCookie(cookie);
		httpServletResponse.addCookie(cookie);
		redirectToLogin(httpServletResponse);
	}

}
