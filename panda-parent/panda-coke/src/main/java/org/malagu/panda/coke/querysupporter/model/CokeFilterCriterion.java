package org.malagu.panda.coke.querysupporter.model;

import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

public class CokeFilterCriterion extends SingleValueFilterCriterion {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String columnName;

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

}
