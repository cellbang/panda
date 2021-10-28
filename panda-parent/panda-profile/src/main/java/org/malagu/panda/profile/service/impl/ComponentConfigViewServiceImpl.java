package org.malagu.panda.profile.service.impl;

import java.util.List;
import org.malagu.panda.profile.ProfileConstants;
import org.malagu.panda.profile.dao.ComponentConfigDao;
import org.malagu.panda.profile.service.ComponentConfigViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComponentConfigViewServiceImpl implements ComponentConfigViewService {
  @Autowired
  private ComponentConfigDao componentConfigDao;


  @Override
  @Cacheable(cacheNames = ProfileConstants.CACHE_VIEW)
  public List<String> loadControlList(String profileKey, String viewName) {
    return componentConfigDao.loadControlList(profileKey, viewName);
  }

  @Override
  @CacheEvict(cacheNames = ProfileConstants.CACHE_VIEW)
  public void evictCache(String profileKey, String viewName) {

  }


}
