package org.malagu.panda.coke.model;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import com.bstek.dorado.annotation.PropertyDef;

@MappedSuperclass
public abstract class BaseModel<K> implements IBase<K> {

  /**
   * 
   */
  private static final long serialVersionUID = -4982852528700515370L;
  @PropertyDef(label = "创建人")
  private K createUser;
  @PropertyDef(label = "创建日期")
  private Date createDate;
  @PropertyDef(label = "更新人")
  private K updateUser;
  @PropertyDef(label = "更新日期")
  private Date updateDate;

  @PropertyDef(label = "有效")
  private Boolean deleted = false;

  @Override
  public K getCreateUser() {
    return createUser;
  }

  @Override
  public void setCreateUser(K createUser) {
    this.createUser = createUser;
  }

  @Override
  public Date getCreateDate() {
    return createDate;
  }

  @Override
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Override
  public K getUpdateUser() {
    return updateUser;
  }

  @Override
  public void setUpdateUser(K updateUser) {
    this.updateUser = updateUser;
  }

  @Override
  public Date getUpdateDate() {
    return updateDate;
  }

  @Override
  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public Boolean getDeleted() {
    return deleted;
  }

  @Override
  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

}
