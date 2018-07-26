package org.malagu.panda.coke.service;

public interface DataTypeConverter {

  /**
   * 尝试将一个任意类型的值转换成本DataType所描述的类型。<br>
   * 如果传入的数据无法被转换将抛出{@link com.bstek.dorado.data.type.DataConvertException}异常。
   * 
   * @param value 要转换的数据。
   * @return 转换后得到的数据。
   */
  Object fromObject(Object value);

  /**
   * 将一个数据对象转换成文本型的值。
   * 
   * @param value 数据对象。
   * @return 文本型的值。
   */
  String toText(Object value);

  Class<?> getMatchType();
}
