package org.malagu.panda.coke.frequency.service;

import org.malagu.panda.coke.frequency.model.BusinessInvokeSetting;

/**
 * 业务调用配置缓存service
 * @author sr on 2019-01-25 
 */
public interface BusinessInvokeSettingCacheService {
  
  /**
   * 根据code获取业务调用配置
   * @param code
   * @return
   * @author sr on 2019-01-25 
   */
  BusinessInvokeSetting getBusinessInvokeSetting(String code);
  
  /**
   * 重新设置业务调用配置缓存
   * @param setting
   * @return
   * @author sr on 2019-01-25 
   */
  BusinessInvokeSetting putBusinessInvokeSetting(BusinessInvokeSetting setting);
  
  /**
   * 清除对应code的业务调用配置缓存
   * @param code
   * @author sr on 2019-01-25 
   */
  void evictBusinessInvokeSetting(String code);
  
  /**
   * 清除所有业务调用配置缓存
   * @author sr on 2019-01-25 
   */
  void evictAllBusinessInvokeSetting();

}
