/**
 * 
 */
package org.malagu.panda.coke.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author xobo
 *
 */
@MappedSuperclass
public class CokeBaseModel extends BaseModel<Long> {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Long id;

  @Override
  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }


  @Override
  public void setId(Long id) {
    this.id = id;
  }

}
