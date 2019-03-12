package org.malagu.panda.security.access.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

/**
 * 菜单安全元数据源
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月22日
 */
@Component
public class UrlSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private List<FilterConfigAttributeProvider> providers;
	
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getRequestMap()
				.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		try {
			for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getRequestMap()
				.entrySet()) {
				if (entry.getKey().matches(request)) {
					return entry.getValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
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
	
	@Autowired
	public void setProvider(List<FilterConfigAttributeProvider> providers) {     
	    AnnotationAwareOrderComparator.sort(providers);
	    this.providers = providers;
	}

}
