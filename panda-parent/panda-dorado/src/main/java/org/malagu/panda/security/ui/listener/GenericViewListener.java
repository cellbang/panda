package org.malagu.panda.security.ui.listener;

import org.malagu.panda.security.ui.filter.ViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;

@Component
public class GenericViewListener extends GenericObjectListener<View> {

  @Value("${panda.appName}")
  private String applicationTitle;

  @Autowired
  private ViewFilter viewFilter;

  @Override
  public boolean beforeInit(View view) throws Exception {
    return true;
  }

  @Override
  public void onInit(View view) throws Exception {
    String title = view.getTitle();
    if (StringUtils.isEmpty(title)) {
      view.setTitle(applicationTitle);
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.isAuthenticated() && auth.getPrincipal() instanceof String
        && "anonymousUser".equals(auth.getPrincipal())) {
      return;
    }
    viewFilter.invoke(view);
  }

}
