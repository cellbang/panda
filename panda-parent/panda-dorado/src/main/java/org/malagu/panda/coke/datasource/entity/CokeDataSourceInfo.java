package org.malagu.panda.coke.datasource.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.malagu.panda.coke.model.CokeBaseModel;


/**
 * 数据源配置信息
 * @author sr on 2019-01-02 
 */
@Entity
@Table(name = "ck_data_source_info")
public class CokeDataSourceInfo extends CokeBaseModel implements Serializable {

  private static final long serialVersionUID = 1L;
  
  /** 数据源名称（唯一） **/
  private String name;
  
  private String url;
  
  private String username;
  
  private String password;
  
  private String type;
  
  private String driverClassName;
  
  private String jndiName;
  
  private Boolean shared;

  private Integer depletionIndex = 0;

  @Column(name = "NAME_")
  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  @Column(name = "URL_")
  public String getUrl() {
      return url;
  }

  public void setUrl(String url) {
      this.url = url;
  }

  @Column(name = "USERNAME_")
  public String getUsername() {
      return username;
  }

  public void setUsername(String username) {
      this.username = username;
  }

  @Column(name = "PASSWORD_")
  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }

  @Column(name = "TYPE_")
  public String getType() {
      return type;
  }

  public void setType(String type) {
      this.type = type;
  }

  @Column(name = "DRIVER_CLASS_NAME_")
  public String getDriverClassName() {
      return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
      this.driverClassName = driverClassName;
  }

  @Column(name = "JNDI_NAME_")
  public String getJndiName() {
      return jndiName;
  }

  public void setJndiName(String jndiName) {
      this.jndiName = jndiName;
  }

  @Column(name = "SHARED_")
  public Boolean isShared() {
      return shared;
  }

  public void setShared(Boolean shared) {
      this.shared = shared;
  }

  @Column(name = "DEPLETION_INDEX_")
  public Integer getDepletionIndex() {
      return depletionIndex;
  }

  public void setDepletionIndex(Integer depletionIndex) {
      this.depletionIndex = depletionIndex;
  }

}

