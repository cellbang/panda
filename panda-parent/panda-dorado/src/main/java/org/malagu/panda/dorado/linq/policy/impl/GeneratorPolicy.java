package org.malagu.panda.dorado.linq.policy.impl;

import java.lang.reflect.Field;

/**
 *@author Kevin.yang
 *@since 2015年5月18日
 */
public interface GeneratorPolicy {
	void apply(Object entity, Field field);
	
}
