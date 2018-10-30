package org.malagu.panda.coke.utility;

public class TimeCost {
  private String name;
  private long timeStart;
  private long timeEnd;

  public TimeCost() {
    timeStart = System.currentTimeMillis();
  }

  public TimeCost(String name) {
    this.name = name;
    this.timeStart = System.currentTimeMillis();
  }

  public long end() {
    timeEnd = System.nanoTime();
    return timeEnd - timeStart;
  }

  @Override
  public String toString() {
    if (name == null) {
      name = "";
    }
    if (timeEnd == 0) {
      end();
    }
    return name + " cost " + (timeEnd - timeStart) + "ms";
  }


}
