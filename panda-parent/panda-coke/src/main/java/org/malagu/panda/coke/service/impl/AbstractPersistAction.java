package org.malagu.panda.coke.service.impl;

import org.hibernate.Session;
import org.malagu.panda.coke.model.IBase;
import org.malagu.panda.coke.service.PersistAction;

public class AbstractPersistAction<T> implements PersistAction<T> {

  @Override
  public void beforeCreate(Session session, IBase<T> entity, IBase<T> parent) {

  }

  @Override
  public void afterCreate(Session session, IBase<T> entity, IBase<T> parent) {

  }

  @Override
  public void beforeUpdate(Session session, IBase<T> entity, IBase<T> parent) {

  }

  @Override
  public void beforeDelete(Session session, IBase<T> entity, IBase<T> parent) {

  }

}
