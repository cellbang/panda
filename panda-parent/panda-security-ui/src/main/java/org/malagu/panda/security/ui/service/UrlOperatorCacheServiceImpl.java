package org.malagu.panda.security.ui.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.security.Constants;
import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.orm.UrlOperator;
import org.malagu.panda.security.ui.utils.SecurityUiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.widget.action.AjaxAction;
import com.bstek.dorado.view.widget.action.UpdateAction;
import com.bstek.dorado.view.widget.data.DataSet;

/**
 * 菜单（url）提供服务的权限控制的缓存实现类
 * 
 * @author sr on 2019-01-23
 */
@Service
public class UrlOperatorCacheServiceImpl implements UrlOperatorCacheService {

  @Autowired
  private UrlOperatorService urlOperatorService;

  @Autowired
  private UrlService urlService;

  @Autowired
  private ViewConfigManager viewConfigManager;

  @Override
  @Transactional
  @Cacheable(value = SecurityUiConstants.URL_OPERATOR_CACHE_NAME,
      keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
  public Map<String, Set<String>> getUrlOperatorMap() {
    Map<String, Set<String>> urlOperatorMap = new HashMap<String, Set<String>>(200);
    List<UrlOperator> urlOperatorList = urlOperatorService.findAll();
    urlOperatorList.forEach(urlOperator -> {
      String service = urlOperator.getService();
      String urlPath = urlOperator.getPath();
      if (StringUtils.isBlank(service)) {
        this.putUrpOperatorMap(urlPath, urlOperatorMap);
        return;
      }
      urlOperatorMap.computeIfAbsent(service, k -> new HashSet<>()).add(urlPath);
    });
    // 所有页面
    List<Url> urlList = urlService.findAll();
    urlList.forEach(url -> {
      this.putUrpOperatorMap(url.getPath(), urlOperatorMap);
    });
    return urlOperatorMap;
  }

  private void putUrpOperatorMap(String urlPath, Map<String, Set<String>> urlOperatorMap) {
    String viewName = urlPath;
    if (StringUtils.isEmpty(viewName)) {
      return;
    }
    viewName = StringUtils.substringBeforeLast(viewName, ".d");
    try {
      ViewConfig viewConfig = viewConfigManager.getViewConfig(viewName);
      View view = viewConfig.getView();
      if (view == null) {
        return;
      }
      String beanName = StringUtils.uncapitalize(StringUtils.substringAfterLast(viewName, "."));
      Collection<ViewElement> viewElements = view.getViewElements();
      viewElements.forEach(viewElement -> {
        String service = null;
        if (viewElement instanceof DataSet) {
          DataSet ds = (DataSet) viewElement;
          DataProvider dp = ds.getDataProvider();
          service = dp == null ? null : dp.getId();
        } else if (viewElement instanceof UpdateAction) {
          UpdateAction ua = (UpdateAction) viewElement;
          DataResolver dr = ua.getDataResolver();
          service = dr == null ? null : dr.getId();
        } else if (viewElement instanceof AjaxAction) {
          AjaxAction aa = (AjaxAction) viewElement;
          service = aa.getService();
        }
        if (StringUtils.isEmpty(service)) {
          return;
        }
        if (service.startsWith(SecurityUiConstants.SERVICE_SEPARATOR)) {
          service = beanName + service;
        }
        urlOperatorMap.computeIfAbsent(service, k -> new HashSet<>()).add(urlPath);
      });
    } catch (Exception e) {
    }
  }

  @Override
  @CacheEvict(value = SecurityUiConstants.URL_OPERATOR_CACHE_NAME,
      keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
  public void evictUrlOperatorMap() {}

}
