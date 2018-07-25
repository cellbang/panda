package org.malagu.panda.importer.converter;

public interface TypeConverter {
	
	Object fromText(Class<?> type, String text);
	
	boolean support(Class<?> clazz);
}
