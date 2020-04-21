package org.malagu.panda.coke.viewgenerator.domain;

import javax.persistence.Entity;
import org.malagu.panda.coke.model.CokeDetailModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity
public class Property extends CokeDetailModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @PropertyDef(label = "属性")
  private String name;
  @PropertyDef(label = "名称")
  private String label;
  @PropertyDef(label = "数据类型")
  private String dataType;
  @PropertyDef(label = "默认值")
  private String defaultValue;
  @PropertyDef(label = "展示格式")
  private String displayFormat;
  @PropertyDef(label = "必填")
  private Boolean required;
  @PropertyDef(label = "标签")
  private String tags;
  @PropertyDef(label = "描述")
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getDisplayFormat() {
    return displayFormat;
  }

  public void setDisplayFormat(String displayFormat) {
    this.displayFormat = displayFormat;
  }

  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Property [name=" + name + ", label=" + label + ", dataType=" + dataType
        + ", defaultValue=" + defaultValue + ", displayFormat=" + displayFormat + ", required="
        + required + ", tags=" + tags + ", description=" + description + "]";
  }

}
