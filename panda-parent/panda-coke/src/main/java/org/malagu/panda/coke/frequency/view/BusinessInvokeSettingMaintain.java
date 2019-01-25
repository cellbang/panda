package org.malagu.panda.coke.frequency.view;

import java.util.List;
import java.util.Map;

import org.malagu.panda.coke.frequency.model.BusinessInvokeSetting;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingCacheService;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * 业务调用配置maintain
 * @author sr on 2019-01-25 
 */
@Component
public class BusinessInvokeSettingMaintain {
  
  @Autowired
  private BusinessInvokeSettingService businessInvokeSettingService;
  
  @Autowired
  private BusinessInvokeSettingCacheService businessInvokeSettingCacheService;
  
  
  @DataProvider
  @Transactional(readOnly = true)
  public void loadBusinessInvokeSettings(Page<BusinessInvokeSetting> page, Criteria criteria, Map<String, Object> parameterMap) {
    businessInvokeSettingService.loadBusinessInvokeSettings(page, criteria, parameterMap);
  }
  
  @DataResolver
  @Transactional
  public void saveBusinessInvokeSetting(List<BusinessInvokeSetting> businessInvokeSettings) {
    businessInvokeSettingService.saveBusinessInvokeSetting(businessInvokeSettings);
  }
  
  @Expose
  public void flushCache() {
    businessInvokeSettingCacheService.evictAllBusinessInvokeSetting();
    businessInvokeSettingService.initBusinessInvokeSetting();
  }

}
