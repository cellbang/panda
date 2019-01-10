package org.malagu.panda.coke.entity;

import com.bstek.dorado.annotation.PropertyDef;

public class PinyinConverter {
  @PropertyDef(label = "实体类")
  private String clazz;
  @PropertyDef(label = "属性")
  private String property;
  @PropertyDef(label = "全拼")
  private String quanpinProperty;
  @PropertyDef(label = "简拼")
  private String jianpinProperty;
  @PropertyDef(label = "批量块")
  private Integer batchSize;

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getQuanpinProperty() {
    return quanpinProperty;
  }

  public void setQuanpinProperty(String quanpinProperty) {
    this.quanpinProperty = quanpinProperty;
  }

  public String getJianpinProperty() {
    return jianpinProperty;
  }

  public void setJianpinProperty(String jianpinProperty) {
    this.jianpinProperty = jianpinProperty;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
  }

}
