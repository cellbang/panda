package org.malagu.panda.security.ui.aspect;


import java.util.Map;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.malagu.panda.security.ContextUtils;
import org.malagu.panda.security.ui.service.UrlOperatorCacheService;
import org.malagu.panda.security.ui.service.UrlService;
import org.malagu.panda.security.ui.utils.SecurityUiConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * dorado提供服务，访问权限控制
 * @author sr on 2019-01-23 
 */
//@Component
//@Aspect
public class DoradoServiceAspect implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Autowired
  private UrlOperatorCacheService urlOperatorCacheService;

  @Autowired
  private UrlService urlService;

  @Around("@annotation(com.bstek.dorado.annotation.DataProvider) "
      + "|| @annotation(com.bstek.dorado.annotation.DataResolver) "
      + "|| @annotation(com.bstek.dorado.annotation.Expose)")
  public Object executeService(ProceedingJoinPoint point) throws Throwable {
    Signature signature = point.getSignature();
    Class<?> clazz = signature.getDeclaringType();
    String beanName = applicationContext.getBeanNamesForType(clazz)[0];
    String method = signature.getName();
    String service = beanName + SecurityUiConstants.SERVICE_SEPARATOR + method;
    Map<String, Set<String>> map = urlOperatorCacheService.getUrlOperatorMap();
    Set<String> urlSet = map.get(service);
    if (urlSet == null) {
      return point.proceed();
    }
    Boolean isAuthority = urlService.checkUrlAuthority(urlSet, ContextUtils.getLoginUser().getUsername());
    isAuthority = true;
    if (!isAuthority) {
      throw new RuntimeException(service + "没有权限调用");
    }
    return point.proceed();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
