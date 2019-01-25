package org.malagu.panda.coke.frequency.service;

import java.util.List;
import java.util.Map;

import org.malagu.panda.coke.frequency.model.BusinessInvokeSetting;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * 业务调用配置service
 * @author sr on 2019-01-25 
 */
public interface BusinessInvokeSettingService {
  /**
   * 初始化业务调用配置缓存
   * @author sr on 2019-01-25 
   */
  void initBusinessInvokeSetting();
  
  /**
   * 分页查询业务调用配置
   * @param page
   * @param criteria
   * @param parameterMap
   * @author sr on 2019-01-25 
   */
  void loadBusinessInvokeSettings(Page<BusinessInvokeSetting> page, Criteria criteria, Map<String, Object> parameterMap);
  
  /**
   * 获取业务调用配置集合
   * @param parameterMap
   * @return
   * @author sr on 2019-01-25 
   */
  List<BusinessInvokeSetting> loadBusinessInvokeSettings(Map<String, Object> parameterMap);
  
  /**
   * 保存业务调用配置集合
   * @param businessInvokeSettings
   * @author sr on 2019-01-25 
   */
  void saveBusinessInvokeSetting(List<BusinessInvokeSetting> businessInvokeSettings);
  
  

}
