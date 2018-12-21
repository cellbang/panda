package org.malagu.panda.coke.filestorage.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "CK_FILE_SHARE")
public class CokeFileShare {
  private Long id;
  private String shareCode;
  private Long fileId;
  private String fileNo;
  private Date validateDate;
  private Integer balanceTimes;
  private Integer totalTimes;

  @Id
  @GeneratedValue
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getShareCode() {
    return shareCode;
  }

  public void setShareCode(String shareCode) {
    this.shareCode = shareCode;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getFileNo() {
    return fileNo;
  }

  public void setFileNo(String fileNo) {
    this.fileNo = fileNo;
  }

  public Integer getBalanceTimes() {
    return balanceTimes;
  }

  public void setBalanceTimes(Integer balanceTimes) {
    this.balanceTimes = balanceTimes;
  }

  public Integer getTotalTimes() {
    return totalTimes;
  }

  public void setTotalTimes(Integer totalTimes) {
    this.totalTimes = totalTimes;
  }

  public Date getValidateDate() {
    return validateDate;
  }

  public void setValidateDate(Date validateDate) {
    this.validateDate = validateDate;
  }

}
