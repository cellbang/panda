package org.malagu.panda.coke.model;

import java.io.Serializable;

public class DictEntry implements Serializable, Comparable<DictEntry> {
  /**
   * 
   */
  private static final long serialVersionUID = -5969110130239793167L;
  private Object key;
  private Object value;

  public DictEntry() {

  }

  public DictEntry(Object val) {
    value = key = val;
  }

  public DictEntry(Object key, Object value) {
    this.key = key;
    this.value = value;
  }

  public Object getKey() {
    return key;
  }

  public void setKey(Object key) {
    this.key = key;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public int compareTo(DictEntry o) {
    return getKey().toString().compareTo(o.getKey().toString());
  }
}
