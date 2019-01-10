package org.malagu.panda.coke.querysupporter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.type.DataType;

public class PropertyWrapper {
  private Class<?> type;
  private String property;
  private String columnName;
  private FilterOperator filterOperator;
  private DataType dataType;
  private boolean autoCreate = true;

  private Collection<String> unionPropertyList = new ArrayList<String>();

  public Object parseValue(Object value) {
    return dataType != null ? dataType.fromObject(value) : value;
  }

  public PropertyWrapper(String property, Class<?> type, FilterOperator filterOperator) {
    this.property = property;
    this.type = type;
    this.filterOperator = filterOperator;
  }

  public void addUnionProperty(String... property) {
    unionPropertyList.addAll(Arrays.asList(property));
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public FilterOperator getFilterOperator() {
    return filterOperator;
  }

  public void setFilterOperator(FilterOperator filterOperator) {
    this.filterOperator = filterOperator;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public Collection<String> getUnionPropertyList() {
    return unionPropertyList;
  }

  public void setUnionPropertyList(Collection<String> unionPropertyList) {
    this.unionPropertyList = unionPropertyList;
  }

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public boolean isAutoCreate() {
    return autoCreate;
  }

  public void setAutoCreate(boolean autoCreate) {
    this.autoCreate = autoCreate;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

}
