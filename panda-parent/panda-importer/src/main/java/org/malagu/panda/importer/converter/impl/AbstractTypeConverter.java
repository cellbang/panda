package org.malagu.panda.importer.converter.impl;

import java.util.Objects;
import org.malagu.panda.importer.converter.TypeConverter;

public abstract class AbstractTypeConverter implements TypeConverter {

  public abstract Object fromText(Class<?> type, String text);


  @Override
  public Object fromObject(Class<?> type, Object value) {
    if (value == null) {
      return null;
    } else if (type.isAssignableFrom(value.getClass())) {
      return value;
    } else {
      return fromText(type, Objects.toString(value, null));
    }
  }


}
