package org.malagu.panda.security.access.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.malagu.panda.security.access.provider.FilterConfigAttributeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javassist.expr.Instanceof;

/**
 * 菜单安全元数据源
 * 
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月22日
 */
@Component
public class UrlSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private List<FilterConfigAttributeProvider> providers;

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return getPathMatcher().getAllConfigAttributes();
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		return getPathMatcher().match(request);
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	class PathMatcher {
		/**
		 * uri 直接匹配
		 */
		private Map<String, Collection<ConfigAttribute>> uriMap = new HashMap<>();
		
		/**
		 * ant style 匹配，按ant style长度排序
		 */
		private Map<RequestMatcher, Collection<ConfigAttribute>> antPathMatcherMap = new TreeMap<RequestMatcher, Collection<ConfigAttribute>>(
				(r1, r2) -> {
					if (r1 instanceof AntPathRequestMatcher && r2 instanceof AntPathRequestMatcher) {
						return Integer.compare(((AntPathRequestMatcher) r1).getPattern().length(),
								((AntPathRequestMatcher) r2).getPattern().length());
					}
					return 0;
				});

		public void add(String path, Collection<ConfigAttribute> configAttributeList) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (path.indexOf('*') > -1) {
				antPathMatcherMap.put(new AntPathRequestMatcher(path, null), configAttributeList);
			} else {
				uriMap.put(path, configAttributeList);
			}
		}

		public Collection<ConfigAttribute> match(HttpServletRequest request) {
			String requestPath = getRequestPath(request);
			Collection<ConfigAttribute> configAttributes = uriMap.get(requestPath);
			if (!CollectionUtils.isEmpty(configAttributes)) {
				return configAttributes;
			}
			for (Entry<RequestMatcher, Collection<ConfigAttribute>> entry : antPathMatcherMap.entrySet()) {
				if (entry.getKey().matches(request)) {
					return entry.getValue();
				}
			}
			return null;
		}

		public Collection<ConfigAttribute> getAllConfigAttributes() {
			Collection<ConfigAttribute> configAttributes = new HashSet<ConfigAttribute>();
			for (Collection<ConfigAttribute> configAttributeList : uriMap.values()) {
				configAttributes.addAll(configAttributeList);
			}

			for (Collection<ConfigAttribute> configAttributeList : antPathMatcherMap.values()) {
				configAttributes.addAll(configAttributeList);
			}

			return configAttributes;
		}

	}

	public PathMatcher getPathMatcher() {
		PathMatcher pathMatcher = new PathMatcher();
		for (FilterConfigAttributeProvider provider : providers) {
			Map<String, Collection<ConfigAttribute>> map = provider.provide();
			if (map != null && !map.isEmpty()) {
				for (Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
					pathMatcher.add(entry.getKey(), entry.getValue());
				}
			}
		}
		return pathMatcher;
	}

	public Map<RequestMatcher, Collection<ConfigAttribute>> getRequestMap() {
		Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new HashMap<>();
		for (FilterConfigAttributeProvider provider : providers) {
			Map<String, Collection<ConfigAttribute>> map = provider.provide();
			if (map != null && !map.isEmpty()) {
				for (Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
					requestMap.put(new AntPathRequestMatcher("/" + entry.getKey(), null), entry.getValue());
				}
			}
		}
		return requestMap;
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			url = StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
		}
		return url;
	}

	@Autowired
	public void setProvider(List<FilterConfigAttributeProvider> providers) {
		AnnotationAwareOrderComparator.sort(providers);
		this.providers = providers;
	}

}
