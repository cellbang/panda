package org.malagu.panda.security.access.provider.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.malagu.panda.security.Constants;
import org.malagu.panda.security.access.provider.FilterConfigAttributeProvider;
import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
/**
 * 默认菜单权限信息提供者
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@Component
@Order(100)
public class UrlFilterConfigAttribueProvider implements
		FilterConfigAttributeProvider  {
	
	@Autowired(required = true)
	private UrlService urlService;
	

	@Override
	@Cacheable(cacheNames = Constants.REQUEST_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<ConfigAttribute>> provide() {
		List<Url> urls = urlService.findAll();
		Map<String, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (Url url : urls) {
			if (validate(url)) {
				requestMap.put(url.getPath(), url.getAttributes());
			}
		}
		return requestMap;
	}

	protected boolean validate(Url url) {
		if (StringUtils.isEmpty(url.getPath())) {
			return false;
		}
		return true;
	}

}
