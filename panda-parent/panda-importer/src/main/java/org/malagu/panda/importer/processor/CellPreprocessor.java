package org.malagu.panda.importer.processor;

import org.malagu.panda.importer.policy.Context;

/**
 *@author Kevin.yang
 *@since 2015年8月30日
 */
public interface CellPreprocessor {

	void process(Context context);
	
	boolean support(Context context);
}
