package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;

public class ShortTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class<?> type, String text) {
    if (StringUtils.isBlank(text)) {
      if (Short.class.isAssignableFrom(type)) {
        return null;
      } else {
        return 0;
      }
    }
    return Short.parseShort(text);
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Short.class.isAssignableFrom(clazz) || short.class.isAssignableFrom(clazz);
  }

}
