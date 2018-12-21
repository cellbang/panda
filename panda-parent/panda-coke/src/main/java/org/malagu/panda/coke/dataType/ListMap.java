package org.malagu.panda.coke.dataType;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多值Map
 * 
 * @author Bing
 * 
 * @param <K> 键
 * @param <V> 值集合类型
 */
public class ListMap<K, V> {
  private Map<K, Collection<V>> data;

  public ListMap(Map<K, Collection<V>> data) {
    this.data = data;
  }

  public static <K, V> ListMap<K, V> hashMap() {
    return new ListMap<K, V>(new HashMap<K, Collection<V>>());
  }

  public static <K, V> ListMap<K, V> linkedHashMap() {
    return new ListMap<K, V>(new LinkedHashMap<K, Collection<V>>());
  }

  public static <K, V> ListMap<K, V> concurrentHashMap() {
    return new ListMap<K, V>(new ConcurrentHashMap<K, Collection<V>>());
  }

  public Map<K, Collection<V>> getData() {
    return data;
  }

  public void setData(Map<K, Collection<V>> data) {
    this.data = data;
  }

  /**
   * 添加到key对应的集合内
   * 
   * @param key
   * @param value
   * @return
   */
  public ListMap<K, V> add(K key, V value) {
    Collection<V> collection = data.get(key);
    if (collection == null) {
      collection = new LinkedHashSet<V>();
      data.put(key, collection);
    }
    collection.add(value);
    return this;
  }

  public ListMap<K, V> addAll(K key, Collection<V> value) {
    Collection<V> collection = data.get(key);
    if (collection == null) {
      collection = new LinkedHashSet<V>();
      data.put(key, collection);
    }
    collection.addAll(value);
    return this;
  }

  public Collection<V> remove(K key) {
    return data.remove(key);
  }

  public boolean removeValue(K key, V value) {
    Collection<V> list = data.get(key);
    return list != null ? list.remove(value) : false;
  }

  /**
   * 键值对应的集合
   * 
   * @param k
   * @return
   */
  public Collection<V> getValue(K k) {
    return data.get(k);
  }

}
