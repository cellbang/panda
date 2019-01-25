package org.malagu.panda.coke.frequency.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 方法调用次数控制配置
 * @author sr on 2019-01-24 
 */
@Entity(name = "PANDA_BUSINESS_INVOKE_SETTING")
public class BusinessInvokeSetting implements Serializable{
  
  /**  **/
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "ID_", length = 64)
  private String id;
  
  /** 编码 **/
  @Column(name = "CODE_", length = 64)
  private String code;
  
  /** 单位 **/
  @Column(name = "UNIT_", columnDefinition = "int not null comment '单位：1年，2月，3周，4天'")
  private Integer unit;
  
  /** 次数 **/
  @Column(name = "NUM_", length = 64, columnDefinition = "bigint(20) not null comment '限制次数'")
  private Long num;
  
  public BusinessInvokeSetting() {
    super();
  }

  public BusinessInvokeSetting(String code, Integer unit, Long num) {
    super();
    this.code = code;
    this.unit = unit;
    this.num = num;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code == null ? null : code.trim();
  }

  public Integer getUnit() {
    return unit;
  }

  public void setUnit(Integer unit) {
    this.unit = unit;
  }

  public Long getNum() {
    return num;
  }

  public void setNum(Long num) {
    this.num = num;
  }

}
