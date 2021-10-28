package org.malagu.panda.profile.service;

import java.util.List;


public interface ComponentConfigViewService {

  List<String> loadControlList(String profileKey, String viewName);

  void evictCache(String profileKey, String viewName);

}
