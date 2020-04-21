package org.malagu.panda.coke.datamask.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.malagu.panda.coke.model.CokeBaseModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "PANDA_DATAMASK_POJO")
public class DataMaskPojo extends CokeBaseModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @PropertyDef(label = "名称")
  @Column(name = "NAME_", length = 64)
  private String name;

  @PropertyDef(label = "描述")
  @Column(name = "DESCRIPTION_", length = 255)
  private String description;

  @PropertyDef(label = "实体")
  @Column(name = "CLAZZ_", length = 64)
  private String clazz;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

}
