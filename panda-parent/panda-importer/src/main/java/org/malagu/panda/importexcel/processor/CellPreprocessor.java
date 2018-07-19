package org.malagu.panda.importexcel.processor;

import org.malagu.panda.importexcel.policy.Context;

/**
 *@author Kevin.yang
 *@since 2015年8月30日
 */
public interface CellPreprocessor {

	void process(Context context);
	
	boolean support(Context context);
}
