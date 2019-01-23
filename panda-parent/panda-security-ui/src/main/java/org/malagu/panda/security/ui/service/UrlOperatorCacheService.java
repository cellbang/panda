package org.malagu.panda.security.ui.service;

import java.util.Map;
import java.util.Set;


/**
 * 菜单（url）提供服务的权限控制的缓存service
 * @author sr on 2019-01-23 
 */
public interface UrlOperatorCacheService {
  
  /**
   * 获取所有菜单服务需要权限控制的map
   * @return key：服务，value：所属菜单
   * @author sr on 2019-01-23 
   */
  Map<String, Set<String>> getUrlOperatorMap();
  
  /**
   * 清除菜单服务权限控制缓存
   * @author sr on 2019-01-23 
   */
  void evictUrlOperatorMap();
}
