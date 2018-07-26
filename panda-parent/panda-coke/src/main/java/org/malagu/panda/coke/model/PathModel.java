package org.malagu.panda.coke.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.bstek.dorado.annotation.PropertyDef;

@MappedSuperclass
public interface PathModel<T> extends IDetail<T> {
  public static final String TypeSeparator = "/";

  @Column(unique = true)
  @PropertyDef(label = "层级编码", description = "当前层级唯一")
  public Long getIndexNo();

  public void setIndexNo(Long indexNo);

  @PropertyDef(label = "序号")
  public Long getOrderNo();

  public void setOrderNo(Long orderNo);

  @PropertyDef(label = "唯一路径")
  public String getTypePath();

  public void setTypePath(String typePath);

  public String getType();

}
