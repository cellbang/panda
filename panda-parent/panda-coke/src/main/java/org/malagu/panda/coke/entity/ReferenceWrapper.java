package org.malagu.panda.coke.entity;

public class ReferenceWrapper {
  private String property;
  private Class<?> clazz;

  public ReferenceWrapper() {}

  public ReferenceWrapper(String property, Class<?> clazz) {
    this.property = property;
    this.clazz = clazz;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }
}
