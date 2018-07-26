package org.malagu.panda.coke.model;

import java.io.Serializable;
import java.util.Date;

public interface IBase<T> extends Serializable {
  public T getId();

  public void setId(T id);

  public String getCreateUser();

  public void setCreateUser(String createUser);

  public Date getCreateDate();

  public void setCreateDate(Date createDate);

  public String getUpdateUser();

  public void setUpdateUser(String updateUser);

  public Date getUpdateDate();

  public void setUpdateDate(Date updateDate);

  public Boolean getDeleted();

  public void setDeleted(Boolean deleted);
}
