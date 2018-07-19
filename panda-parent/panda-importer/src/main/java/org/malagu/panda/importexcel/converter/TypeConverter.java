package org.malagu.panda.importexcel.converter;

public interface TypeConverter {
	
	Object fromText(Class<?> type, String text);
	
	boolean support(Class<?> clazz);
}
