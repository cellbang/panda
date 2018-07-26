package org.malagu.panda.coke.querysupporter.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryResolver {
  private Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
  private StringBuffer assemblySql = new StringBuffer();
  private StringBuilder select = new StringBuilder();
  private StringBuilder from = new StringBuilder();
  private StringBuilder where = new StringBuilder();
  private StringBuilder order = new StringBuilder();

  public QueryResolver merge(QueryResolver other) {
    if (other == null) {
      return this;
    }
    select = concatenate(select, other.select, ", ");
    from = concatenate(from, other.from, ", ");
    where = concatenate(where, other.where, " and ");
    order = concatenate(order, other.order, ", ");
    this.valueMap.putAll(other.valueMap);
    return this;
  }

  private StringBuilder concatenate(StringBuilder left, StringBuilder right, String concatenator) {
    if (left.length() == 0) {
      return left.append(right);
    } else if (right.length() == 0) {
      return left;
    } else {
      return left.append(concatenator).append(right);
    }
  }


  public Map<String, Object> getValueMap() {
    return valueMap;
  }

  public void setValueMap(Map<String, Object> valueMap) {
    this.valueMap = valueMap;
  }

  public StringBuffer getAssemblySql() {
    return assemblySql;
  }

  public void setAssemblySql(StringBuffer assemblySql) {
    this.assemblySql = assemblySql;
  }

  public StringBuilder getSelect() {
    return select;
  }

  public void setSelect(StringBuilder select) {
    this.select = select;
  }

  public StringBuilder getFrom() {
    return from;
  }

  public void setFrom(StringBuilder from) {
    this.from = from;
  }

  public StringBuilder getWhere() {
    return where;
  }

  public void setWhere(StringBuilder where) {
    this.where = where;
  }

  public StringBuilder getOrder() {
    return order;
  }

  public void setOrder(StringBuilder order) {
    this.order = order;
  }

  public String getOrderPhrase() {
    if (order.length() > 0) {
      return " order by " + order;
    } else {
      return "";
    }
  }
}
