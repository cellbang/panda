package org.malagu.panda.coke.datamask.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.malagu.panda.coke.model.CokeBaseModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "PANDA_DATAMASK_RULE")
public class DataMaskRule extends CokeBaseModel {
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

  @PropertyDef(label = "脱敏正则")
  @Column(name = "REGEX_", length = 64)
  private String regex;

  @PropertyDef(label = "需要隐藏的组")
  @Column(name = "HIDDEN_GROUP", length = 64)
  private String hiddenGroup;

  @PropertyDef(label = "替换字符")
  @Column(name = "MASK", length = 64)
  private String mask;

  @PropertyDef(label = "替换正则")
  @Column(name = "REPLACEMENT", length = 64)
  private String replacement;

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

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public String getHiddenGroup() {
    return hiddenGroup;
  }

  public void setHiddenGroup(String hiddenGroup) {
    this.hiddenGroup = hiddenGroup;
  }

  public String getMask() {
    return mask;
  }

  public void setMask(String mask) {
    this.mask = mask;
  }

  public String getReplacement() {
    return replacement;
  }

  public void setReplacement(String replacement) {
    this.replacement = replacement;
  }

}
