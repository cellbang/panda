package org.malagu.panda.security.ui.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.xobo.dorado.exposedservice.security.service.DoradoExposedServiceWhiteListProvider;

@Service
public class PandaDoradoExposedServiceWhiteListProvider
    implements DoradoExposedServiceWhiteListProvider {

  private Collection<String> whiteList = new ArrayList<String>();

  public PandaDoradoExposedServiceWhiteListProvider() {
    whiteList.add("templateController#loadGlobal");
    whiteList.add("noticeController#connectServer");
  }


  @Override
  public Collection<String> getWhiteList() {

    return whiteList;
  }

}
