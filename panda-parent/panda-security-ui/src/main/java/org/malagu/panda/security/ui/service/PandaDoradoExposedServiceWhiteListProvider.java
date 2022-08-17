package org.malagu.panda.security.ui.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.xobo.dorado.exposedservice.security.api.DoradoExposedServiceWhiteListProvider;

@Service
public class PandaDoradoExposedServiceWhiteListProvider
    implements DoradoExposedServiceWhiteListProvider {

  private Collection<String> whiteList = new ArrayList<String>();

  public PandaDoradoExposedServiceWhiteListProvider() {
    whiteList.add("templateController#loadGlobal");
    whiteList.add("noticeController#connectServer");
    whiteList.add("frameworkController#loadUrlForLoginUser");
  }


  @Override
  public Collection<String> getWhiteList() {

    return whiteList;
  }

}
