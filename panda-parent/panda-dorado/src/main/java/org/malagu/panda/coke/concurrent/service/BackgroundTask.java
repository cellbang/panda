package org.malagu.panda.coke.concurrent.service;

import java.util.Map;

public interface BackgroundTask {

  boolean cancelTask();

  Map<String, Object> process(Map<String, Object> parameter);

}
