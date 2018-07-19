package org.malagu.panda.importexcel.parser.impl;

import org.malagu.panda.importexcel.Constants;
import org.malagu.panda.importexcel.model.MappingRule;
import org.malagu.panda.importexcel.parser.CellPostParser;
import org.malagu.panda.importexcel.policy.Context;

import net.sf.cglib.beans.BeanMap;

/**
 *@author Kevin.yang
 *@since 2015年8月30日
 */
public class DefaultCellPostParser implements CellPostParser {

	@Override
	public String getName() {
		return "默认后置解析器";
	}

	@Override
	public void parse(Context context) {
		if (context.getValue() != null) {
			MappingRule mappingRule =context.getCurrentMappingRule();
			BeanMap beanMap = BeanMap.create(context.getCurrentEntity());
			if (context.getValue() != Constants.IGNORE_ERROR_FORMAT_DATA) {
				beanMap.put(mappingRule.getPropertyName(), context.getValue());
			}
		}
	}

}
