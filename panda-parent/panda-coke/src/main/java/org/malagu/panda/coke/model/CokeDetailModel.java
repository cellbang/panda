/**
 * 
 */
package org.malagu.panda.coke.model;

import javax.persistence.MappedSuperclass;
import com.bstek.dorado.annotation.PropertyDef;

/**
 * @author xobo
 *
 */
@MappedSuperclass
public class CokeDetailModel extends CokeBaseModel implements IDetail<Long> {
  @PropertyDef(label = "父编码")
  private Long parentId = 0L;
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

}
