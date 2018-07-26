package org.malagu.panda.coke.model;

public class Pinyin {
  private String quan;
  private String jian;

  public Pinyin() {}

  public Pinyin(String quan) {
    jian = quan.substring(0, 1);
    this.quan = quan;
  }

  public Pinyin(String quan, String jian) {
    this.quan = quan;
    this.jian = jian;
  }

  public Pinyin append(String quan) {
    return new Pinyin(this.quan + quan, jian + quan.substring(0, 1));
  }

  public String getQuan() {
    return quan;
  }

  public void setQuan(String quan) {
    this.quan = quan;
  }

  public String getJian() {
    return jian;
  }

  public void setJian(String jian) {
    this.jian = jian;
  }

  @Override
  public String toString() {
    return "Pinyin [quan=" + quan + ", jian=" + jian + "]";
  }

}
