package org.malagu.panda.security.ui.configure;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malagu.panda.security.WebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@Component
@Order(120)
@ConditionalOnMissingBean(WebSecurityConfigurer.class)
public class DoradoWebSecurityConfigurer extends WebSecurityConfigurer {
		
		private static final String URL_PREFIX = "/";

		@Value("${panda.loginPath}")
		private String loginPath;
		
		@Value("${panda.logoutPath}")
		private String logoutPath;
		
		@Value("${panda.systemAnonymous}")
		private String systemAnonymous;
		
		@Value("${panda.customAnonymous}")
		private String customAnonymous;

		@Value("${panda.maximumSessions:}")
		private Integer maximumSessions;

		@Value("${panda.alwaysUseDefaultTargetUrl:true}")
		private Boolean alwaysUseDefaultTargetUrl;

		@Value("${panda.defaultTargetUrl:/}")
		private String defaultTargetUrl;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			http.authorizeRequests()
					.antMatchers(mergeAnonymous())
					.permitAll()
					.anyRequest()
					.authenticated()
				.and()
					.formLogin().successHandler(new CustomAuthenticationSuccessHandler(alwaysUseDefaultTargetUrl, defaultTargetUrl))
					.loginPage(URL_PREFIX + loginPath)
					.permitAll()
				.and()
					.logout()
					.logoutUrl(URL_PREFIX + logoutPath)
					.permitAll()
				.and()
					.rememberMe();
					
			if (maximumSessions!=null) {
			  http.sessionManagement().maximumSessions(maximumSessions);
			}
			
			super.configure(http);

		}
		
		private String[] mergeAnonymous() {
			String[] anonymous = null;
			if (StringUtils.hasText(systemAnonymous) && StringUtils.hasText(customAnonymous)) {
				anonymous = (systemAnonymous + "," + customAnonymous).split(",");
			} else if (StringUtils.hasText(systemAnonymous)) {
				anonymous = systemAnonymous.split(",");
			}  else if (StringUtils.hasText(customAnonymous)) {
				anonymous = customAnonymous.split(",");
			}
			return anonymous;
		}
		
		class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

		

      private RequestCache requestCache = new HttpSessionRequestCache();
			
			public CustomAuthenticationSuccessHandler(boolean alwaysUseDefaultTargetUrl, String defaultTargetUrl) {
			  this.setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
			  this.setDefaultTargetUrl(defaultTargetUrl);
				this.setRequestCache(requestCache);
			}
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws ServletException, IOException {
				SavedRequest savedRequest = requestCache.getRequest(request, response);
				if (savedRequest != null && savedRequest.getRedirectUrl().endsWith("/dorado/view-service")) {
					this.getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
				} else {
					super.onAuthenticationSuccess(request, response, authentication);
				}
			}
			
		}
		
	}


