package org.malagu.panda.coke.model;

import java.io.Serializable;

public abstract class SuperBaseModel<K> implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5225401883124763824L;

  public abstract K getId();

  public abstract void setId(K id);
}
