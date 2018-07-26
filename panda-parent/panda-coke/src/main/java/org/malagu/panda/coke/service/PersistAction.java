package org.malagu.panda.coke.service;

import org.hibernate.Session;
import org.malagu.panda.coke.model.IBase;

public interface PersistAction<T> {
  void beforeCreate(Session session, IBase<T> entity, IBase<T> parent);

  void afterCreate(Session session, IBase<T> entity, IBase<T> parent);

  void beforeUpdate(Session session, IBase<T> entity, IBase<T> parent);

  void beforeDelete(Session session, IBase<T> entity, IBase<T> parent);
}
