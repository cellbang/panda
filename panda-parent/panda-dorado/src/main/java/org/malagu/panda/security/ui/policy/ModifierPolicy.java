package org.malagu.panda.security.ui.policy;

import java.lang.reflect.Field;

import org.malagu.panda.dorado.linq.policy.impl.AbstractModifiedGeneratorPolicy;
import org.malagu.panda.security.ContextUtils;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年8月10日
 */
public class ModifierPolicy extends AbstractModifiedGeneratorPolicy {

	@Override
	protected Object getValue(Object entity, Field field) {
		return ContextUtils.getLoginUsername();
	}

}
