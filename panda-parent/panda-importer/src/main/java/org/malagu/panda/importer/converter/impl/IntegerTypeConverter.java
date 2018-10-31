package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;


public class IntegerTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class<?> type, String text) {
    if (StringUtils.isBlank(text)) {
      if (Integer.class.isAssignableFrom(type)) {
        return null;
      } else {
        return 0;
      }
    }
    return Integer.parseInt(text);
  }

  @Override
  public boolean support(Class<?> clazz) {
    return int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz);
  }

}
