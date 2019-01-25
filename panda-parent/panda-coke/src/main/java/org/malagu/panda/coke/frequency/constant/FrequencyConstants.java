package org.malagu.panda.coke.frequency.constant;

/**
 * 常量类
 * @author sr on 2019-01-24 
 */
public class FrequencyConstants {
  
  /** 方法调用控制缓存cacheName **/
  public static final String METHOD_INVOKE_SETTING_CACHE_NAME = "MethodInvokeSetting";
  
  public static final String METHOD_INVOKE_COUNT_CACHE_NAME = "MethodInvokeCount";
  
  public static final String METHOD_SEPARATOR = "#";

  /**
   * 调用次数控制单位
   */
  public enum Unit {
    /** 年 **/
    YEAR(1),
    /** 月 **/
    MONTH(2),
    /** 周 **/
    WEEKEND(3), 
    /** 天 **/
    DAY(4);
    private Integer value;
    
    Unit(Integer value) {
      this.value = value;
    }
    public Integer getValue() {
      return value;
    }
    
    public static Unit getUnit(Integer value) {
      switch (value) {
        case 1:
          return YEAR;
        case 2:
          return MONTH;
        case 3:
          return WEEKEND;
        default:
          return DAY;
      }
    }
  }
}
