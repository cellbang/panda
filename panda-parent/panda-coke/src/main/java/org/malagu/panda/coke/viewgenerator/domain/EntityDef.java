package org.malagu.panda.coke.viewgenerator.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.model.CokeDetailModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity
public class EntityDef extends CokeDetailModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @PropertyDef(label = "名称")
  private String name;
  @PropertyDef(label = "中文名")
  private String caption;
  @PropertyDef(label = "实体类")
  private String clazz;
  @PropertyDef(label = "描述")
  private String description;
  private Boolean master = true;;
  private String dataSet;
  private String currentDataPath;
  private String listDataPath;
  private String parentName;
  private String listName;
  private String parameter;
  private Integer pageSize;

  private Collection<EntityDef> children = new ArrayList<EntityDef>();
  private Collection<Property> props;
  private Collection<Property> queryProps;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  @Transient
  public Collection<Property> getProps() {
    return props;
  }

  public void setProps(Collection<Property> props) {
    this.props = props;
  }

  @Transient
  public Collection<Property> getQueryProps() {
    return queryProps;
  }

  public void setQueryProps(Collection<Property> queryProps) {
    this.queryProps = queryProps;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDataSet() {
    return dataSet;
  }

  public void setDataSet(String dataSet) {
    this.dataSet = dataSet;
  }

  public String getCurrentDataPath() {
    return currentDataPath;
  }

  public void setCurrentDataPath(String currentDataPath) {
    this.currentDataPath = currentDataPath;
  }

  public String getListDataPath() {
    return listDataPath;
  }

  public void setListDataPath(String listDataPath) {
    this.listDataPath = listDataPath;
  }

  public Boolean getMaster() {
    return master;
  }

  public void setMaster(Boolean master) {
    this.master = master;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  public String getListName() {
    return listName;
  }

  public void setListName(String listName) {
    this.listName = listName;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }



  @Transient
  public Collection<EntityDef> getChildren() {
    return children;
  }

  public void setChildren(Collection<EntityDef> children) {
    this.children = children;
  }

  @Transient
  public List<Map<String, String>> getParameterList() {
    if (StringUtils.isEmpty(parameter)) {
      return Collections.emptyList();
    }
    String[] mapValueArr = parameter.split(",");
    List<Map<String, String>> list = new ArrayList<Map<String, String>>(mapValueArr.length);
    for (String string : mapValueArr) {
      String[] map = string.split("=");
      Map<String, String> entry = new HashMap<String, String>();
      entry.put("name", map[0]);
      entry.put("value", map[1]);
      list.add(entry);
    }
    return list;
  }

  @Override
  public String toString() {
    return "EntityDef [name=" + name + ", caption=" + caption + ", clazz=" + clazz
        + ", description=" + description + ", master=" + master + ", dataSet=" + dataSet
        + ", currentDataPath=" + currentDataPath + ", listDataPath=" + listDataPath
        + ", parentName=" + parentName + ", listName=" + listName + ", parameter=" + parameter
        + ", pageSize=" + pageSize + ", props=" + props + ", queryProps=" + queryProps + "]";
  }

}
