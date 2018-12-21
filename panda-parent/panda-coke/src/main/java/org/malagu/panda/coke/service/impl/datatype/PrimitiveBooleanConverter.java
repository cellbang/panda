package org.malagu.panda.coke.service.impl.datatype;

public class PrimitiveBooleanConverter extends BooleanConverter {
  @Override
  public Object fromObject(Object value) {
    if (value == null) {
      return Boolean.FALSE;
    } else {
      return super.fromObject(value);
    }
  }

  @Override
  public Object fromText(String text) {
    if (text == null) {
      return Boolean.FALSE;
    } else {
      return super.fromText(text);
    }
  }

  @Override
  public Class<?> getMatchType() {
    return boolean.class;
  }

}
