package org.malagu.panda.coke.filestorage.domain;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

@Entity(name = "CK_BLOB")
public class CokeBlob {
  private Long id;
  private byte[] data;
  private Date createTime;

  @Id
  @GeneratedValue
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Lob
  @Column(name = "DATA")
  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Transient
  public String getString() {
    if (data == null) {
      return null;
    }
    return new String(data, StandardCharsets.UTF_8);
  }
}
