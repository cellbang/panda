package org.malagu.panda.dorado.linq.filter;

import org.malagu.panda.dorado.linq.policy.LinqContext;

/**
 *@author Kevin.yang
 *@since 2015年6月11日
 */
public interface Filter {
	boolean invoke(LinqContext linqContext);
}
