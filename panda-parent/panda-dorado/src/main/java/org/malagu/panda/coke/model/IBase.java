package org.malagu.panda.coke.model;

import java.io.Serializable;
import java.util.Date;

public interface IBase<T> extends Serializable {
  public T getId();

  public void setId(T id);

  public T getCreateUser();

  public void setCreateUser(T createUser);

  public Date getCreateDate();

  public void setCreateDate(Date createDate);

  public T getUpdateUser();

  public void setUpdateUser(T updateUser);

  public Date getUpdateDate();

  public void setUpdateDate(Date updateDate);

  public Boolean getDeleted();

  public void setDeleted(Boolean deleted);
}
