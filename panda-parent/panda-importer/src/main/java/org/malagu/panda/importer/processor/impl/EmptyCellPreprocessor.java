package org.malagu.panda.importer.processor.impl;

import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.processor.CellPreprocessor;

/**
 *@author Kevin.yang
 *@since 2015年9月2日
 */
public class EmptyCellPreprocessor implements CellPreprocessor {

	@Override
	public void process(Context context) {

	}

	@Override
	public boolean support(Context context) {
		return false;
	}

}
