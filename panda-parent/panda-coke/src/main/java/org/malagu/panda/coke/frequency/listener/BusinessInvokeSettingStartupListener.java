package org.malagu.panda.coke.frequency.listener;

import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingCacheService;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bstek.dorado.core.EngineStartupListener;

/**
 * 启动项目刷新业务调用配置缓存
 * @author sr on 2019-01-25 
 */
@Service
public class BusinessInvokeSettingStartupListener extends EngineStartupListener{
  
  @Autowired
  private BusinessInvokeSettingCacheService businessInvokeSettingCacheService;
  
  @Autowired
  private BusinessInvokeSettingService businessInvokeSettingService;
  
  @Override
  public void onStartup() throws Exception {
    // 刷新业务调用配置缓存
    businessInvokeSettingCacheService.evictAllBusinessInvokeSetting();
    // 设置业务调用缓存
    businessInvokeSettingService.initBusinessInvokeSetting();
  }

}
