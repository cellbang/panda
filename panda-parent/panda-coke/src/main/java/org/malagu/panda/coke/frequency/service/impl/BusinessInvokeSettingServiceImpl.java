package org.malagu.panda.coke.frequency.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.coke.frequency.model.BusinessInvokeSetting;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingCacheService;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingService;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.lin.Linq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * 业务调用配置service实现类
 * @author sr on 2019-01-25 
 */
@Service
public class BusinessInvokeSettingServiceImpl implements BusinessInvokeSettingService{
  
  @Autowired
  private BusinessInvokeSettingCacheService businessInvokeSettingCacheService;

  @Override
  @Transactional
  public void initBusinessInvokeSetting() {
    List<BusinessInvokeSetting> settings = this.loadBusinessInvokeSettings(null);
    for (BusinessInvokeSetting setting : settings) {
      businessInvokeSettingCacheService.putBusinessInvokeSetting(setting);
    }
  }

  @Override
  @Transactional
  public void loadBusinessInvokeSettings(Page<BusinessInvokeSetting> page, Criteria criteria,
      Map<String, Object> parameterMap) {
    this.queryBusinessInvokeSettings(page, criteria, parameterMap);
  }

  @Override
  @Transactional
  public List<BusinessInvokeSetting> loadBusinessInvokeSettings(Map<String, Object> parameterMap){
    return this.queryBusinessInvokeSettings(null, null, parameterMap);
  }
  
  private List<BusinessInvokeSetting> queryBusinessInvokeSettings(Page<BusinessInvokeSetting> page, Criteria criteria,
      Map<String, Object> parameterMap){
    Linq linq = JpaUtil.linq(BusinessInvokeSetting.class);
    String code = MapUtils.getString(parameterMap, "code");
    if (StringUtils.isNotBlank(code)) {
      linq.where(criteria).like("code", "%" + code + "%");
    }
    if (page == null) {
      return linq.list();
    }
    linq.paging(page);
    return null;
  }
  
  @Override
  @Transactional
  public void saveBusinessInvokeSetting(List<BusinessInvokeSetting> businessInvokeSettings) {
    for (BusinessInvokeSetting setting : businessInvokeSettings) {
      String code = setting.getCode();
      if (StringUtils.isBlank(code) || setting.getNum() == null || setting.getUnit() == null) {
        throw new RuntimeException("参数不全，不能保存");
      }
      EntityState state = EntityUtils.getState(setting);
      if (state == EntityState.NEW || state == EntityState.MODIFIED) {
        businessInvokeSettingCacheService.putBusinessInvokeSetting(setting);
      }
      if (state == EntityState.DELETED) {
        businessInvokeSettingCacheService.evictBusinessInvokeSetting(code);
      }
    }
    JpaUtil.save(businessInvokeSettings);
  }

}
