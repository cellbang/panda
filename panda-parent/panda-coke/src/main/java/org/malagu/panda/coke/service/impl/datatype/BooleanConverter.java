package org.malagu.panda.coke.service.impl.datatype;

public class BooleanConverter extends AbstractDataTypeConverter {

  public static final String VALUE_TRUE = "true";
  public static final String VALUE_FALSE = "false";

  public static final String VALUE_ON = "on";
  public static final String VALUE_OFF = "off";

  public static final String VALUE_YES = "yes";
  public static final String VALUE_NO = "no";

  public static final String VALUE_1 = "1";
  public static final String VALUE_0 = "0";

  public Object fromText(String text) {
    if (text == null) {
      return null;
    } else if (text.equalsIgnoreCase(VALUE_TRUE) || text.equalsIgnoreCase(VALUE_ON)
        || text.equalsIgnoreCase(VALUE_YES) || text.equalsIgnoreCase(VALUE_1)) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @Override
  public Object fromObject(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Boolean) {
      return value;
    } else if (value instanceof String) {
      return fromText((String) value);
    }
    return Boolean.FALSE;
  }

  @Override
  public Class<?> getMatchType() {
    return Boolean.class;
  }

}
