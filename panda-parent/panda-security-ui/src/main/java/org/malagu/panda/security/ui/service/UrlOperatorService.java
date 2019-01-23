package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.UrlOperator;

/**
 * 菜单（url）服务权限控制service
 * @author sr on 2019-01-23 
 */
public interface UrlOperatorService {
  
  /**
   * 获取所有菜单服务的配置信息
   * @return
   * @author sr on 2019-01-23 
   */
  List<UrlOperator> findAll();

}
