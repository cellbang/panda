package org.malagu.panda.security.access.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.malagu.panda.security.Constants;
import org.malagu.panda.security.access.provider.ComponentProvider;
import org.malagu.panda.security.orm.Component;
import org.malagu.panda.security.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;

/**
 * 默认组件权提供者
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@org.springframework.stereotype.Component
@Order(100)
public class CompenentProviderImpl implements ComponentProvider {

	@Autowired
	private ComponentService componentService;
	
	@Override
	@Cacheable(cacheNames = Constants.COMPONENT_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<Component>> provide() {
		List<Component> components = componentService.findAll();
		Map<String, Collection<Component>> componentMap = new LinkedHashMap<String, Collection<Component>>();
		for (Component component : components) {
			String key = component.getPath();
			Collection<Component> cs = componentMap.get(key);
			if (cs == null) {
				cs = new ArrayList<Component>();
				componentMap.put(key, cs);
			}
			cs.add(component);
		}
		return componentMap;
	}

}
