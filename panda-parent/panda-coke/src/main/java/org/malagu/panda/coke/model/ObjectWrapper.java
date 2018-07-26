package org.malagu.panda.coke.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class ObjectWrapper {
  private Map<String, Object> value;

  public ObjectWrapper() {
    value = new HashMap<String, Object>();
  }

  public ObjectWrapper(Object value) {
    initValue(value);
  }

  @SuppressWarnings("unchecked")
  protected void initValue(Object value) {
    Map<String, Object> entity = new HashMap<String, Object>();
    if (!(value instanceof Map)) {
      try {
        BeanUtils.populate(value, entity);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      entity.putAll((Map<? extends String, ? extends Object>) value);
    }
    this.value = entity;
  }

  public Object get(String key) {
    return value.get(key);
  }

  public void put(String key, Object value) {
    this.value.put(key, value);
  }

}
