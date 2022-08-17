package org.malagu.panda.security.ui.service;

import org.malagu.panda.security.decision.manager.SecurityDecisionManager;
import org.malagu.panda.security.orm.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.dorado.exposedservice.security.api.UrlAuthorizationProvider;

@Service
public class PandaUrlAuthorizationProvider implements UrlAuthorizationProvider {

  @Override
  public boolean authorize(String url) {
    Url pandaUrl = new Url();
    pandaUrl.setPath(url);
    return securityDecisionManager.decide(pandaUrl);
  }

  @Autowired
  private SecurityDecisionManager securityDecisionManager;
}
