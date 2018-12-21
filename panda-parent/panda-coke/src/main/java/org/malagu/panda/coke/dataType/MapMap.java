package org.malagu.panda.coke.dataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapMap<C, K, V> {
  private Map<C, Map<K, V>> data;

  public MapMap(Map<C, Map<K, V>> data) {
    this.data = data;
  }

  public MapMap<C, K, V> add(C c, K k, V v) {
    safeGet(c).put(k, v);
    return this;
  }

  public V get(C c, K k) {
    return safeGet(c).get(k);
  }

  public Map<K, V> firstMapValue() {
    return data.values().iterator().next();
  }

  @SuppressWarnings("unchecked")
  public Map<K, V> safeGet(C c) {
    Map<K, V> kv = data.get(c);
    if (kv == null) {
      try {
        kv = data.getClass().newInstance();
        data.put(c, kv);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return kv;
  }

  public Map<K, V> get(C c) {
    return data.get(c);
  }

  public Map<K, V> remove(C c) {
    return data.remove(c);
  }

  public Map<K, V> put(C c, Map<K, V> map) {
    return data.put(c, map);
  }

  public static <C, K, V> MapMap<C, K, V> hashMap() {
    return new MapMap<C, K, V>(new HashMap<C, Map<K, V>>());
  }

  public static <C, K, V> MapMap<C, K, V> concurrentHashMap() {
    return new MapMap<C, K, V>(new ConcurrentHashMap<C, Map<K, V>>());
  }

  public static <C, K, V> MapMap<C, K, V> LinkedHashMap() {
    return new MapMap<C, K, V>(new LinkedHashMap<C, Map<K, V>>());
  }

  public Map<C, Map<K, V>> getData() {
    return data;
  }

  public void setData(Map<C, Map<K, V>> data) {
    this.data = data;
  }

  public boolean isEmpty() {
    return this.data.isEmpty();
  }

  public Collection<V> getValues() {
    Collection<V> values = new ArrayList<V>();
    for (Map<K, V> valueMap : data.values()) {
      values.addAll(valueMap.values());
    }
    values.removeAll(Collections.singleton(null));
    return values;
  }
}
