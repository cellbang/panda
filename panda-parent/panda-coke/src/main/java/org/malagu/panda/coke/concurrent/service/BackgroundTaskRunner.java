package org.malagu.panda.coke.concurrent.service;

import java.util.Map;
import java.util.concurrent.Callable;

public class BackgroundTaskRunner implements Callable<Map<String, Object>> {
  private BackgroundTask backgroundTask;
  private Map<String, Object> parameter;

  @Override
  public Map<String, Object> call() throws Exception {
    return backgroundTask.process(parameter);
  }

  public void cancelTask() {
    backgroundTask.cancelTask();
  }

  public BackgroundTaskRunner(BackgroundTask backgroundTask, Map<String, Object> parameter) {
    this.backgroundTask = backgroundTask;
    this.parameter = parameter;
  }

}
