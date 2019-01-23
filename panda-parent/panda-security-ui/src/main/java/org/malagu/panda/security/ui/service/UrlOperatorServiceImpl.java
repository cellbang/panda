package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.linq.JpaUtil;
import org.malagu.panda.security.orm.UrlOperator;
import org.springframework.stereotype.Service;

/**
 * 菜单（url）中需要权限控制的服务的配置实现类
 * @author sr on 2019-01-23 
 */
@Service
public class UrlOperatorServiceImpl implements UrlOperatorService{

  @Override
  public List<UrlOperator> findAll() {
    return JpaUtil.linq(UrlOperator.class).list();
  }

}
