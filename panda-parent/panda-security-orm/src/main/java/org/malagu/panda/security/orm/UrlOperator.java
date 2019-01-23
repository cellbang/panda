package org.malagu.panda.security.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 菜单服务权限控制
 * @author sr on 2019-01-23 
 */
@Entity(name = "PANDA_URL_OPERATOR")
public class UrlOperator implements Serializable{

  /**  **/
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID_", length = 64)
  private String id;
  
  @Column(name = "PATH_", length = 512)
  private String path;
  
  @Column(name = "SERVICE_", length = 255)
  private String service;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }


}
