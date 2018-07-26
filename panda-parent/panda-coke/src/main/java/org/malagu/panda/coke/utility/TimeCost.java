package org.malagu.panda.coke.utility;

public class TimeCost {
  private String name;
  private long timeStart;
  private long timeEnd;

  public TimeCost() {
    timeStart = System.nanoTime();
  }

  public TimeCost(String name) {
    this.name = name;
    this.timeStart = System.nanoTime();
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
    return name + " cost " + ((timeEnd - timeStart) / 1000.0) + "ms";
  }


}
