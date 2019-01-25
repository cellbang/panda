package org.malagu.panda.coke.frequency.util;

import org.malagu.panda.coke.frequency.constant.FrequencyConstants.Unit;

/**
 * 工具类
 * @author sr on 2019-01-24 
 */
public class FrequencyUtils {
  
  public static Long getExpireDays(Unit unit) {
    if (unit == Unit.YEAR) {
      return 366L;
    } else if (unit == Unit.MONTH) {
      return 31L;
    } else if (unit == Unit.WEEKEND) {
      return 7L;
    } else {
      return 1L;
    }
  }

}
