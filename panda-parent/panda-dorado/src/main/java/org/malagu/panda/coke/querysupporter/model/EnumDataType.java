package org.malagu.panda.coke.querysupporter.model;



import com.bstek.dorado.data.type.SimpleDataType;

@SuppressWarnings("rawtypes")
public class EnumDataType extends SimpleDataType {
  private Class<? extends Enum> clazz;


  public EnumDataType(Class<? extends Enum> clazz) {
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public Object fromText(String text) {
    return Enum.valueOf(clazz, text);
  }

  @Override
  public Object fromObject(Object value) {
    if (value == null) {
      return null;
    }
    if (value.getClass().equals(clazz)) {
      return value;
    }
    return fromText(value.toString());
  }

}
