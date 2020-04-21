package org.malagu.panda.coke.datamask.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.malagu.panda.coke.model.CokeDetailModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "PANDA_DATAMASK_POJO_PROP")
public class DataMaskPojoProperty extends CokeDetailModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Column(name = "PARENT_ID")
  private Long parentId;

  @PropertyDef(label = "名称")
  @Column(name = "NAME_", length = 64)
  private String name;

  @PropertyDef(label = "描述")
  @Column(name = "DESCRIPTION_", length = 255)
  private String description;

  @PropertyDef(label = "属性")
  @Column(name = "PROPERTY_NAME_", length = 255)
  private String propertyName;

  @Column(name = "DATAMASK_RULE_ID", length = 255)
  private Long dataMaskRuleId;

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

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Long getDataMaskRuleId() {
    return dataMaskRuleId;
  }

  public void setDataMaskRuleId(Long dataMaskRuleId) {
    this.dataMaskRuleId = dataMaskRuleId;
  }


}
