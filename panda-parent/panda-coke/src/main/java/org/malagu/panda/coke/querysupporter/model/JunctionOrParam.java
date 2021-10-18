package org.malagu.panda.coke.querysupporter.model;

import java.util.Map;

public class JunctionOrParam extends JunctionQueryParam {

  public JunctionOrParam() {
    super();
  }

  public JunctionOrParam(String name) {
    super(name);
  }

  public JunctionOrParam(Map<String, Object> queryParameter) {
    super(queryParameter);
  }

  public JunctionOrParam(Map<String, Object> queryParameter, String name) {
    super(queryParameter, name);
  }

  public static JunctionOrParam create() {
    return new JunctionOrParam();
  }

  public static JunctionOrParam create(int count) {
    return new JunctionOrParam(JunctionOrParam.class.getSimpleName() + count);
  }


  @Override
  public String toString() {
    return getQueryParameter().toString();
  }
}
