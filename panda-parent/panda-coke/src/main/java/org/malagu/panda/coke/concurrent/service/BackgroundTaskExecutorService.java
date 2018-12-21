package org.malagu.panda.coke.concurrent.service;

import java.util.Map;


public interface BackgroundTaskExecutorService {

  void cancel(String taskId);

  void execute(String type, String desc, String backgroundTaskBeanId,
      Map<String, Object> parameter);

  void runAgain(String taskId);

}
