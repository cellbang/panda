package org.malagu.panda.coke.service.impl.datatype;

import org.malagu.panda.coke.service.DataTypeConverter;

public abstract class AbstractDataTypeConverter implements DataTypeConverter {

  @Override
  public String toText(Object value) {
    return value.toString();
  }

}
