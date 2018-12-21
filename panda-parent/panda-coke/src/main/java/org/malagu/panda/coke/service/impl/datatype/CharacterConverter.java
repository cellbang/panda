package org.malagu.panda.coke.service.impl.datatype;

import com.bstek.dorado.data.type.DataConvertException;

public class CharacterConverter extends AbstractDataTypeConverter {

  @Override
  public Class<?> getMatchType() {
    return Character.class;
  }

  public Object fromText(String text) {
    if (text == null || text.length() == 0) {
      return null;
    } else {
      return new Character(text.charAt(0));
    }
  }

  @Override
  public Object fromObject(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Character) {
      return value;
    } else if (value instanceof String) {
      return fromText((String) value);
    } else {
      throw new DataConvertException(value.getClass(), Character.class);
    }
  }

}
