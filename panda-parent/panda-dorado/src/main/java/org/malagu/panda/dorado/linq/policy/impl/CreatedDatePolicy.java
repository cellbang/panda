package org.malagu.panda.dorado.linq.policy.impl;

import java.lang.reflect.Field;
import java.util.Date;

/**
 *@author Kevin.yang
 *@since 2015年5月18日
 */
public class CreatedDatePolicy extends AbstractNewGeneratorPolicy {

	@Override
	protected Object getValue(Object entity, Field field) {
		return new Date();
	}


}
