package org.malagu.panda.importexcel.converter.impl;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.importexcel.converter.TypeConverter;

public class LongTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Long.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0;
			}
		}
		return Long.parseLong(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Long.class.isAssignableFrom(clazz)||long.class.isAssignableFrom(clazz);
	}
}
