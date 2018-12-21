package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;

public class ByteTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class<?> type, String text) {
    if (StringUtils.isBlank(text)) {
      if (Byte.class.isAssignableFrom(type)) {
        return null;
      } else {
        return 0;
      }
    }
    return Byte.parseByte(text);
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Byte.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz);
  }
}
