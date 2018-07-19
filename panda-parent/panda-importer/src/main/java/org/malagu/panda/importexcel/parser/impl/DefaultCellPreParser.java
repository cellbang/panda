package org.malagu.panda.importexcel.parser.impl;

import org.malagu.panda.importexcel.parser.CellPreParser;
import org.malagu.panda.importexcel.policy.Context;

/**
 *@author Kevin.yang
 *@since 2015年8月30日
 */
public class DefaultCellPreParser implements CellPreParser {

	@Override
	public String getName() {
		return "默认前置解析器";
	}

	@Override
	public void parse(Context context) {
		context.setValue(context.getCurrentCell().getValue());
	}

}
