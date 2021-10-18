package org.malagu.panda.coke.querysupporter.model;

import java.util.HashMap;
import java.util.Map;

public class JunctionQueryParam {
  private Map<String, Object> queryParameter;
  private String name;

  public JunctionQueryParam(String name) {
    this.name = name;
    initQueryParameter();
  }

  public JunctionQueryParam(Map<String, Object> queryParameter) {
    this.queryParameter = queryParameter;
    initQueryParameter();
  }

  public JunctionQueryParam(Map<String, Object> queryParameter, String name) {
    this.queryParameter = queryParameter;
    this.name = name;
    initQueryParameter();
  }

  private void initQueryParameter() {
    if (queryParameter == null) {
      this.queryParameter = new HashMap<String, Object>();
    }

  }

  public JunctionQueryParam() {
    initQueryParameter();
  }

  public JunctionQueryParam add(String property, Object value) {
    queryParameter.put(property, value);
    return this;
  }

  public JunctionQueryParam remove(String property) {
    queryParameter.remove(property);
    return this;
  }

  public Map<String, Object> getQueryParameter() {
    return queryParameter;
  }

  public static JunctionQueryParam create() {
    return new JunctionQueryParam();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
