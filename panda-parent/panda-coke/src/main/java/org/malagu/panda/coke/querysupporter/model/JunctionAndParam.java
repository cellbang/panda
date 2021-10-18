package org.malagu.panda.coke.querysupporter.model;

import java.util.Map;

public class JunctionAndParam extends JunctionQueryParam {

  public JunctionAndParam(Map<String, Object> queryParameter) {
    super(queryParameter);
  }

  public JunctionAndParam() {
    super();
  }

  public JunctionAndParam(String name) {
    super(name);
  }


  public JunctionAndParam(Map<String, Object> queryParameter, String name) {
    super(queryParameter, name);
  }

  public static JunctionAndParam create() {
    return new JunctionAndParam();
  }

  public static JunctionAndParam create(int count) {
    return new JunctionAndParam(JunctionAndParam.class.getSimpleName() + count);
  }

  @Override
  public String toString() {
    return getQueryParameter().toString();
  }
}
