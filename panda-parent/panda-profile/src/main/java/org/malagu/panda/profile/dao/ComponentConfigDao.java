package org.malagu.panda.profile.dao;

import java.util.Collection;
import java.util.List;

import org.malagu.panda.profile.domain.ComponentConfig;

import com.bstek.dorado.data.variant.Record;


public interface ComponentConfigDao {

  List<String> loadControlList(String profileKey, String viewName);


  void removeComponentProfileByControlId(String profileKey, String controlId);

  List<ComponentConfig> loadComponentConfigs(String profileKey, String viewName);

  void saveComponentProfile(String profileKey, String controlId, String cols,
      Collection<Record> members);


  ComponentConfig loadComponentConfig(String profileKey, String controlId);

}
