package org.malagu.panda.security.access.provider.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.malagu.panda.security.Constants;
import org.malagu.panda.security.access.provider.ComponentConfigAttributeProvider;
import org.malagu.panda.security.orm.Component;
import org.malagu.panda.security.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;

/**
 * 默认组件权限信息提供者
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@org.springframework.stereotype.Component
@Order(100)
public class CompenentConfigAttributeProviderImpl implements
		ComponentConfigAttributeProvider {

	@Autowired
	private ComponentService componentService;
	
	@Override
	@Cacheable(cacheNames = Constants.COMPONENT_ATTRIBUTE_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<ConfigAttribute>> provide() {
		List<Component> components = componentService.findAll();
		Map<String, Collection<ConfigAttribute>> componentMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (Component component : components) {
			String key = component.toString();
			Collection<ConfigAttribute> attributes = componentMap.get(key);
			if (attributes == null) {
				attributes = component.getAttributes();
				componentMap.put(key, attributes);
			} else {
				if (component.getAttributes() != null) {
					attributes.addAll(component.getAttributes());
				}
			}
			
			
			componentMap.put(component.getPath(), Collections.<ConfigAttribute> emptyList());
		}
		return componentMap;
	}

}
