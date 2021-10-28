package org.malagu.panda.profile.dao.impl;

import java.util.*;
import java.util.stream.Collectors;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.profile.dao.ComponentConfigDao;
import org.malagu.panda.profile.domain.ComponentConfig;
import org.malagu.panda.profile.domain.ComponentConfigMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bstek.dorado.data.variant.Record;

@Service
@Transactional(readOnly = true)
public class ComponentConfigDaoImpl implements ComponentConfigDao {

  @Override
  public List<ComponentConfig> loadComponentConfigs(String profileKey, String viewName) {
    List<ComponentConfig> componentConfigs = JpaUtil.linq(ComponentConfig.class)
        .equal("name", profileKey).like("controlId", viewName + ".%").list();
    if (!componentConfigs.isEmpty()) {
      List<ComponentConfigMember> list =
          loadComponentConfigMembers(JpaUtil.collectId(componentConfigs));
      Map<String, List<ComponentConfigMember>> map =
          list.stream().collect(Collectors.groupingBy(ComponentConfigMember::getComponentConfigId));

      componentConfigs.forEach(componentConfig -> componentConfig
          .setComponentConfigMembers(map.get(componentConfig.getId())));
    }

    return componentConfigs;
  }
  
  @Override
  public ComponentConfig loadComponentConfig(String profileKey, String controlId) {
    List<ComponentConfig> componentConfigs = JpaUtil.linq(ComponentConfig.class)
        .equal("name", profileKey).equal("controlId", controlId).list();
    ComponentConfig componentConfig = null;
    if (!componentConfigs.isEmpty()) {
      
      componentConfig = componentConfigs.get(0);
      List<ComponentConfigMember> members = loadComponentConfigMembers(componentConfig.getId());
      componentConfig.setComponentConfigMembers(members);
    }

    return componentConfig;
  }

  @Transactional
  @Override
  public void removeComponentProfileByControlId(String profileKey, String controlId) {
    JpaUtil.lind(ComponentConfigMember.class).exists(ComponentConfig.class)
        .equalProperty("id", "componentConfigId").equal("controlId", controlId)
        .equal("name", profileKey).end().delete();
    JpaUtil.lind(ComponentConfig.class).equal("controlId", controlId).equal("name", profileKey)
        .delete();
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

  private List<ComponentConfigMember> loadComponentConfigMembers(Set<String> configIdSet) {
    return JpaUtil.linq(ComponentConfigMember.class).in("componentConfigId", configIdSet)
        .asc("order").list();
  }
  
  public List<ComponentConfigMember> loadComponentConfigMembers(String componentConfigId) {
    return JpaUtil.linq(ComponentConfigMember.class).equal("componentConfigId", componentConfigId)
        .asc("order").list();
  }

  @Override
  @Transactional
  public void saveComponentProfile(String profileKey, String controlId, String cols,
      Collection<Record> members) {

    removeComponentProfileByControlId(controlId, profileKey);

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

  @Override
  public List<String> loadControlList(String profileKey, String viewName) {

    return JpaUtil.linq(ComponentConfig.class).select("controlId").equal("name", profileKey)
        .like("controlId", viewName + ".%").groupBy("controlId").list();

  }

}
