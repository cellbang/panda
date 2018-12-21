package org.malagu.panda.coke.model;

public class SkinInfo {
  private String name;
  private String desc;
  private boolean ie6;

  public SkinInfo() {

  }

  public SkinInfo(String name) {
    this.name = name;
    desc = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public boolean isIe6() {
    return ie6;
  }

  public void setIe6(boolean ie6) {
    this.ie6 = ie6;
  }

}
