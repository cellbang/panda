package org.malagu.panda.coke.frequency.service;

import org.malagu.panda.coke.frequency.constant.FrequencyConstants.Unit;

/**
 * 方法调用控制service
 * @author sr on 2019-01-24 
 */
public interface BusinessInvokeService {
  
  /**
   * 获取当前调用次数
   * @param key
   * @param unit
   * @return
   * @author sr on 2019-01-25 
   */
  Long getCurrentNum(String key, Unit unit);

}
