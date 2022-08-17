package org.malagu.panda.security.ui.events.listener;

import org.malagu.panda.security.ui.events.PandaUrlUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.xobo.dorado.exposedservice.security.service.DoradoExposedServiceViewMappingService;

@Component
public class PandaUrlUpdatedEventListener implements ApplicationListener<PandaUrlUpdatedEvent> {
  @Override
  public void onApplicationEvent(PandaUrlUpdatedEvent event) {
    doradoExposedServiceViewParserService.evictCache();
  }

  @Autowired
  private DoradoExposedServiceViewMappingService doradoExposedServiceViewParserService;
}
