package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;



public class StringTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class<?> type, String text) {
    if (StringUtils.isBlank(text)) {
      return null;
    }
    return text;
  }

  @Override
  public boolean support(Class<?> clazz) {
    return clazz.isAssignableFrom(String.class);
  }

}
