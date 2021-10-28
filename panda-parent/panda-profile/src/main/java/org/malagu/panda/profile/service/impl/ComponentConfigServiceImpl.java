package org.malagu.panda.profile.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.profile.ProfileConstants;
import org.malagu.panda.profile.dao.ComponentConfigDao;
import org.malagu.panda.profile.domain.ComponentConfig;
import org.malagu.panda.profile.domain.ComponentConfigMember;
import org.malagu.panda.profile.service.ComponentConfigService;
import org.malagu.panda.profile.service.ComponentConfigViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bstek.dorado.data.variant.Record;

@Service
@Transactional(readOnly = true)
public class ComponentConfigServiceImpl implements ComponentConfigService {
  @Autowired
  private ComponentConfigDao componentConfigDao;



  @Override
  public ComponentConfig loadComponentConfig(String profileKey, String controlId) {
    return componentConfigDao.loadComponentConfig(profileKey, controlId);
  }

  @Override
  @Cacheable(cacheNames = ProfileConstants.CACHE_COMPONENT)
  public List<ComponentConfig> loadComponentConfigs(String profileKey, String viewName) {
    return componentConfigDao.loadComponentConfigs(profileKey, viewName);
  }



  @Override
  public List<String> loadControlList(String profileKey, String viewName) {
    return componentConfigDao.loadControlList(profileKey, viewName);
  }

  private String getViewByControlId(String controlId) {
    return controlId.substring(0, controlId.lastIndexOf('.'));
  }


  @Transactional
  @Override
  @CacheEvict(cacheNames = ProfileConstants.CACHE_COMPONENT)
  public void removeComponentProfileByControlId(String profileKey, String controlId) {


    JpaUtil.lind(ComponentConfigMember.class).exists(ComponentConfig.class)
        .equalProperty("id", "componentConfigId").equal("controlId", controlId)
        .equal("name", profileKey).end().delete();
    JpaUtil.lind(ComponentConfig.class).equal("controlId", controlId).equal("name", profileKey)
        .delete();

  }

  @Override
  @CacheEvict(cacheNames = ProfileConstants.CACHE_COMPONENT)
  public void evictCache(String profileKey, String controlId) {
    componentConfigViewService.evictCache(profileKey, getViewByControlId(controlId));
  }

  private ComponentConfigMember record2ConfigMember(ComponentConfig componentConfig,
      Record record) {
    ComponentConfigMember componentConfigMember = new ComponentConfigMember();
    componentConfigMember.setId(UUID.randomUUID().toString());
    componentConfigMember.setControlType(record.getString("controlType"));
    componentConfigMember.setControlName(record.getString("controlName"));
    componentConfigMember.setOrder(record.getInt("order"));
    componentConfigMember.setParentControlName(record.getString("parentControl"));
    componentConfigMember.setCaption(record.getString("caption"));
    componentConfigMember.setWidth(record.getString("width"));
    componentConfigMember.setColSpan(record.getInt("colSpan"));
    componentConfigMember.setRowSpan(record.getInt("rowSpan"));
    componentConfigMember.setVisible(record.getBoolean("visible"));
    componentConfigMember.setComponentConfigId(componentConfig.getId());
    return componentConfigMember;
  }


  @Override
  @Transactional
  public void saveComponentProfile(String profileKey, String controlId, String cols,
      Collection<Record> members) {

    removeComponentProfileByControlId(profileKey, controlId);

    ComponentConfig componentConfig = new ComponentConfig();
    componentConfig.setId(UUID.randomUUID().toString());
    componentConfig.setName(profileKey);
    componentConfig.setCols(cols);
    componentConfig.setControlId(controlId);

    if (members != null && !members.isEmpty()) {
      JpaUtil.persist(componentConfig);
      for (Record record : members) {
        JpaUtil.persist(record2ConfigMember(componentConfig, record));
      }
    }
  }

  @Autowired
  private ComponentConfigViewService componentConfigViewService;


}
