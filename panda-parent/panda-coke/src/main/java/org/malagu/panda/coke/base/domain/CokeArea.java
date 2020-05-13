package org.malagu.panda.coke.base.domain;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import com.bstek.dorado.annotation.PropertyDef;

@Entity(name = "ck_area")
@Table(name = "ck_area",
    indexes = {@Index(columnList = "deep", name = "IDX_DEEP"), @Index(columnList = "pid", name = "IDX_PID")})
public class CokeArea {
  /**
   * 
   */
  private Long id;
  private Long pid;
  @PropertyDef(label = "深度", description = "层级深度；0：省，1：市，2：区，3：镇")
  private Integer deep;
  @PropertyDef(label = "名称")
  private String name;
  @PropertyDef(label = "拼音前缀")
  private String initial;
  @PropertyDef(label = "拼音缩写")
  private String initials;
  @PropertyDef(label = "拼音")
  private String pinyin;
  @PropertyDef(label = "原始编号")
  private String extId;
  @PropertyDef(label = "原始名称")
  private String extName;

  private Collection<CokeArea> children;

  @Id
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getDeep() {
    return deep;
  }

  public void setDeep(Integer deep) {
    this.deep = deep;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInitial() {
    return initial;
  }

  public void setInitial(String initial) {
    this.initial = initial;
  }

  public String getInitials() {
    if (StringUtils.isEmpty(initials)) {
      initials = WordUtils.initials(pinyin);
    }
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public String getPinyin() {
    if (StringUtils.isEmpty(initials)) {
      return initials;
    }
    return pinyin.replaceAll("\\s+", "");
  }

  public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
  }

  public String getExtId() {
    return extId;
  }

  public void setExtId(String extId) {
    this.extId = extId;
  }

  public String getExtName() {
    return extName;
  }

  public void setExtName(String extName) {
    this.extName = extName;
  }


  public Long getPid() {
    return pid;
  }


  public void setPid(Long pid) {
    this.pid = pid;
  }

  @Transient
  public Collection<CokeArea> getChildren() {
    return children;
  }

  public void setChildren(Collection<CokeArea> children) {
    this.children = children;
  }

  public void addChild(CokeArea cokeArea) {
    if (children == null) {
      children = new ArrayList<CokeArea>();
    }
    children.add(cokeArea);
  }

}
