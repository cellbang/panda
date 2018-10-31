package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;

public class BooleanTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class<?> type, String text) {
    if (StringUtils.isBlank(text)) {
      return false;
    }
    return Boolean.parseBoolean(text);
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
  }

}
