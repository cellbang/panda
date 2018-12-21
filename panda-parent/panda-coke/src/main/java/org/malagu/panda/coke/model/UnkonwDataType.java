package org.malagu.panda.coke.model;

import com.bstek.dorado.data.type.AbstractDataType;

public class UnkonwDataType extends AbstractDataType {

  @Override
  public Object fromText(String text) {
    return text;
  }

  @Override
  public Object fromObject(Object value) {
    return value;
  }

}
