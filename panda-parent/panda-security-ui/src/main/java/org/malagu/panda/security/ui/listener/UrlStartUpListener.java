package org.malagu.panda.security.ui.listener;

import org.malagu.panda.security.ui.service.UrlOperatorCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.core.EngineStartupListener;


/**
 * 项目启动，清除菜单服务的访问权限的缓存
 * @author sr on 2019-01-23 
 */
@Component
public class UrlStartUpListener extends EngineStartupListener{
  
  @Autowired
  private UrlOperatorCacheService urlOperatorCacheService;
  

  @Override
  public void onStartup() throws Exception {
    urlOperatorCacheService.evictUrlOperatorMap();
  }

}
