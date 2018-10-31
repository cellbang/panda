package org.malagu.panda.importer.converter.impl;

import java.util.Objects;
import org.malagu.panda.importer.converter.TypeConverter;

public abstract class AbstractTypeConverter implements TypeConverter {

  public abstract Object fromText(Class<?> type, String text);


  @Override
  public Object fromObject(Class<?> type, Object value) {
    return fromText(type, Objects.toString(value, null));
  }


}
