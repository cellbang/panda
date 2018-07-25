package org.malagu.panda.importer.processor.impl;

import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.processor.CellPostprocessor;

/**
 *@author Kevin.yang
 *@since 2015年9月2日
 */
public class EmptyCellPostprocessor implements CellPostprocessor {

	@Override
	public void process(Context context) {

	}

	@Override
	public boolean support(Context context) {
		return false;
	}

}
