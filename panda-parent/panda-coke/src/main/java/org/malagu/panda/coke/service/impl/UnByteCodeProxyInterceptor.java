package org.malagu.panda.coke.service.impl;

import org.hibernate.EmptyInterceptor;

import com.bstek.dorado.util.proxy.ProxyBeanUtils;


public class UnByteCodeProxyInterceptor extends EmptyInterceptor {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public String getEntityName(Object object) {
    if (object != null) {
      Class<?> cl = ProxyBeanUtils.getProxyTargetType(object);
      return cl.getName();
    } else {
      return null;
    }
  }

}
