package org.malagu.panda.coke.querysupporter.model;

import java.util.HashMap;
import java.util.Map;

public class OrMap {
  private Map<String, Object> conditionMap;

  public OrMap() {
    conditionMap = new HashMap<String, Object>();
  }

  public OrMap add(String property, Object value) {
    conditionMap.put(property, value);
    return this;
  }

  public Map<String, Object> getConditions() {
    return conditionMap;
  }

  public static OrMap create() {
    return new OrMap();
  }
}
