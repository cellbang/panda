package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.importer.converter.TypeConverter;


public class DoubleTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Double.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0f;
			}
		}
		return Double.parseDouble(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Double.class.isAssignableFrom(clazz)||double.class.isAssignableFrom(clazz);
	}
	
}
