package org.malagu.panda.coke.filestorage.domain;

import java.util.Date;
import javax.persistence.Entity;
import org.malagu.panda.coke.model.CokeBaseModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity(name = "CK_FILE_SHARE")
public class CokeFileShare extends CokeBaseModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String shareCode;
  private Long fileId;
  private String fileNo;
  @PropertyDef(label = "有效期")
  private Date validateDate;
  @PropertyDef(label = "可下载次数")
  private Integer balanceTimes;
  private Integer totalTimes;

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
