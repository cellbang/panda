package org.malagu.panda.coke.frequency.service.impl;

import org.malagu.panda.coke.frequency.constant.FrequencyConstants;
import org.malagu.panda.coke.frequency.model.BusinessInvokeSetting;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingCacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 业务调用配置缓存service实现类
 * @author sr on 2019-01-25 
 */
@Service
public class BusinessInvokeSettingCacheServiceImpl implements BusinessInvokeSettingCacheService{

  @Override
  @Cacheable(value = FrequencyConstants.METHOD_INVOKE_SETTING_CACHE_NAME, key = "#code")
  public BusinessInvokeSetting getBusinessInvokeSetting(String code) {
    return null;
  }

  @Override
  @CachePut(value = FrequencyConstants.METHOD_INVOKE_SETTING_CACHE_NAME, key = "#setting.code")
  public BusinessInvokeSetting putBusinessInvokeSetting(BusinessInvokeSetting setting) {
    return setting;
  }

  @Override
  @CacheEvict(value = FrequencyConstants.METHOD_INVOKE_SETTING_CACHE_NAME, key = "#code")
  public void evictBusinessInvokeSetting(String code) {
    
  }
  
  @Override
  @CacheEvict(value = FrequencyConstants.METHOD_INVOKE_SETTING_CACHE_NAME, allEntries = true)
  public void evictAllBusinessInvokeSetting() {
  }

  
}
