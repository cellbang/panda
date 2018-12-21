package org.malagu.panda.coke.service.impl;

import org.hibernate.Session;
import org.malagu.panda.coke.model.IBase;
import org.malagu.panda.coke.service.PersistAction;

public class NopPersistAction implements PersistAction<Object> {

  @Override
  public void afterCreate(Session session, IBase<Object> entity, IBase<Object> parent) {}

  @Override
  public void beforeUpdate(Session session, IBase<Object> entity, IBase<Object> parent) {}

  @Override
  public void beforeDelete(Session session, IBase<Object> entity, IBase<Object> parent) {}

  @Override
  public void beforeCreate(Session session, IBase<Object> entity, IBase<Object> parent) {
    // TODO Auto-generated method stub

  }

}
