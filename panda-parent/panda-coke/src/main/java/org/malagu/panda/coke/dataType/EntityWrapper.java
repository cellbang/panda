package org.malagu.panda.coke.dataType;

public class EntityWrapper<K, V> {
  private K key;
  private V value;

  public EntityWrapper() {}

  @SuppressWarnings("unchecked")
  public EntityWrapper(K key) {
    this.key = key;
    this.value = (V) key;
  }

  public EntityWrapper(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }
}
