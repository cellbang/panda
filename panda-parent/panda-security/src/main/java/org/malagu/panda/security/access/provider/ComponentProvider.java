package org.malagu.panda.security.access.provider;

import java.util.Collection;
import java.util.Map;

import org.malagu.panda.security.orm.Component;

/**
 * 组件提供者
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年8月6日
 */
public interface ComponentProvider {

	Map<String, Collection<Component>> provide();
}
