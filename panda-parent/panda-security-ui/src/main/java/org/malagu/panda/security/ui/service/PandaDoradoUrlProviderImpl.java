package org.malagu.panda.security.ui.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.dorado.exposedservice.security.service.DoradoUrlProvider;

@Service
public class PandaDoradoUrlProviderImpl implements DoradoUrlProvider {

  @Override
  public Collection<String> getUrls() {
    List<Url> urlList = urlService.findAll();
    return urlList.stream().map(Url::getPath).collect(Collectors.toList());
  }

  @Autowired
  private UrlService urlService;

}
