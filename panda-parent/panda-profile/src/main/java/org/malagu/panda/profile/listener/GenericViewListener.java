package org.malagu.panda.profile.listener;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.profile.ProfileConstants;
import org.malagu.panda.profile.domain.ComponentConfig;
import org.malagu.panda.profile.filter.ProfileFilter;
import org.malagu.panda.profile.provider.ProfileKeyProvider;
import org.malagu.panda.profile.service.ComponentConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;

@Component("profile.genericViewListener")
public class GenericViewListener extends GenericObjectListener<View> implements InitializingBean {

  @Autowired
  private List<ProfileFilter<?>> profileFilters;

  @Autowired
  private ComponentConfigService componentConfigService;

  @Autowired
  private ProfileKeyProvider profileKeyProvider;

  @Override
  public boolean beforeInit(View view) throws Exception {
    return true;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void onInit(View view) throws Exception {
    if (!needProfile(view)) {
      return;
    }
    String viewName = view.getName();
    String profileKey = profileKeyProvider.getProfileKey();

    if (StringUtils.isEmpty(profileKey)) {
      return;
    }

   
    List<String> controlIds = componentConfigService.loadControlList(profileKey, viewName);
    if (controlIds.isEmpty()) {
      return;
    }

    for (String controlId : controlIds) {
      ComponentConfig componentConfig =
          componentConfigService.loadComponentConfig(profileKey, controlId);

      ViewElement viewElement =
          view.getViewElement(StringUtils.substringAfterLast(componentConfig.getControlId(), "."));
      if (viewElement != null) {
        for (ProfileFilter profileFilter : profileFilters) {
          if (profileFilter.support(viewElement)) {
            profileFilter.apply(viewElement, componentConfig);
          }
        }
      }
    }

  }

  private boolean needProfile(View view) {
    Map<String, Object> metaData = view.getMetaData();
    if (metaData != null && metaData.containsKey(ProfileConstants.NEED_PROFILE_FLAG_NAME)
        && (Boolean) metaData.get(ProfileConstants.NEED_PROFILE_FLAG_NAME)) {
      return true;
    }
    return false;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    AnnotationAwareOrderComparator.sort(profileFilters);

  }

}
