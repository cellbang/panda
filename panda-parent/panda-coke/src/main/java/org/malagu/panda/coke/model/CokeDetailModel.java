/**
 * 
 */
package org.malagu.panda.coke.model;

import javax.persistence.MappedSuperclass;

/**
 * @author xobo
 *
 */
@MappedSuperclass
public class CokeDetailModel extends CokeBaseModel implements IDetail<Long>{
  private Long parentId;
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.coke.model.IDetail#getParentId()
   */
  @Override
  public Long getParentId() {
    // TODO Auto-generated method stub
    return parentId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.coke.model.IDetail#setParentId(java.lang.Object)
   */
  @Override
  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.coke.model.IDetail#getRoot()
   */
  @Override
  public Long getRoot() {
    // TODO Auto-generated method stub
    return null;
  }

}
