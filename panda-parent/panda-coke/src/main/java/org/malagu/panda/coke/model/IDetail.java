package org.malagu.panda.coke.model;

public interface IDetail<T> extends IBase<T> {
  T getParentId();

  void setParentId(T parentId);

  T getRoot();

}
