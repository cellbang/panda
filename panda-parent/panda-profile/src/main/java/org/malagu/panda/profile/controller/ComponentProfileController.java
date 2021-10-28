package org.malagu.panda.profile.controller;

import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.profile.provider.ProfileKeyProvider;
import org.malagu.panda.profile.service.ComponentConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.variant.Record;

@Controller
public class ComponentProfileController {

  @Autowired
  private ComponentConfigService componentConfigService;

  @Expose
  public void resetComponentProfile(String controlId) {
    String profileKey = profileKeyProvider.getProfileKey();
    componentConfigService.removeComponentProfileByControlId(profileKey, controlId);
  }

  @SuppressWarnings("unchecked")
  @Expose
  public void saveComponentProfile(Map<String, Object> parameter) {
    String profileKey = profileKeyProvider.getProfileKey();
    String controlId = MapUtils.getString(parameter, "controlId");
    String name = MapUtils.getString(parameter, "name");
    String cols = MapUtils.getString(parameter, "cols");
    Collection<Record> members = (Collection<Record>) MapUtils.getObject(parameter, "members");

    componentConfigService.saveComponentProfile(StringUtils.defaultIfEmpty(name, profileKey),
        controlId, cols, members);
    componentConfigService.evictCache(profileKey, controlId);
  }


  @Autowired
  private ProfileKeyProvider profileKeyProvider;


}
