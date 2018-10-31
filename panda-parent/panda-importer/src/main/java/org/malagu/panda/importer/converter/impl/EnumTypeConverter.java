package org.malagu.panda.importer.converter.impl;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EnumTypeConverter extends AbstractTypeConverter {

  @Override
  public Object fromText(Class type, String text) {
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    return Enum.valueOf(type, text);
  }

  @Override
  public boolean support(Class<?> clazz) {
    if (Enum.class.isAssignableFrom(clazz)) {
      return true;
    }
    return false;
  }

}
