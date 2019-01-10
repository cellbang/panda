package org.malagu.panda.coke.concurrent.service;

public interface BackgroundTaskSupport {
  public enum TaskOperation {
    ReStart, Cancel
  };

  String getOperator();

  String getProcessor();

  void control(String taskId, TaskOperation taskOperation);

}
