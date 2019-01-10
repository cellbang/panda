package org.malagu.panda.coke.concurrent.service.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.MapUtils;
import org.malagu.panda.coke.concurrent.service.BackgroundTask;
import org.springframework.stereotype.Service;

@Service
public class TestCounterBackgroundTask implements BackgroundTask {
  private boolean canceled = false;

  @Override
  public Map<String, Object> process(Map<String, Object> parameter) {
    String name = MapUtils.getString(parameter, "name", "unknown");
    int i = 0;
    while (i <= 100 && !canceled) {
      i++;
      System.out.println(name + ": " + i);
      try {
        TimeUnit.SECONDS.sleep(1L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      if (i % 1000 == 0) {
        System.out.println(name + ": " + i);
      }
    }
    return parameter;
  }

  @Override
  public boolean cancelTask() {
    canceled = true;
    return true;
  }

}
