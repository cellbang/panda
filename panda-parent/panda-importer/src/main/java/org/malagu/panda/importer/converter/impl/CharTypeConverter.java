package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;

public class CharTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class<?> type, String text) {
    if (StringUtils.isBlank(text)) {
      if (Character.class.isAssignableFrom(type)) {
        return null;
      } else {
        return ' ';
      }
    }
    return text.charAt(0);
  }

  @Override
  public boolean support(Class<?> clazz) {
    return char.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
  }


}
