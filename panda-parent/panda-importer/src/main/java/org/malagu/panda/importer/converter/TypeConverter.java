package org.malagu.panda.importer.converter;

public interface TypeConverter {

  Object fromObject(Class<?> type, Object value);

  boolean support(Class<?> clazz);
}
