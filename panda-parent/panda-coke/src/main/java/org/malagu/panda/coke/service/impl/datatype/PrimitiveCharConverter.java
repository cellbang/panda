package org.malagu.panda.coke.service.impl.datatype;

public class PrimitiveCharConverter extends CharacterConverter {

  @Override
  public Class<?> getMatchType() {
    return char.class;
  }

  @Override
  public Object fromText(String text) {
    if (text == null) {
      return new Character('\0');
    }
    return super.fromText(text);
  }

  @Override
  public Object fromObject(Object value) {
    if (value == null) {
      return new Character('\0');
    }
    return super.fromObject(value);
  }

}
