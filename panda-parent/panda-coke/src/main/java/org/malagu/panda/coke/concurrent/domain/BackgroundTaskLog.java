package org.malagu.panda.coke.concurrent.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Index;
import com.bstek.dorado.annotation.PropertyDef;

@Entity(name = "CK_BACKGROUND_TASK_LOG")
public class BackgroundTaskLog {
  private Long id;
  @PropertyDef(label = "任务编号")
  private String taskId;
  @PropertyDef(label = "任务类型")
  private String type;
  @PropertyDef(label = "描述")
  private String desc;
  @PropertyDef(label = "任务 BeanID")
  private String backgroundTaskBeanId;
  @PropertyDef(label = "状态")
  private String status;// P processing, S sucess, F fail,C canceled
  @PropertyDef(label = "参数")
  private String parameter;
  @PropertyDef(label = "结果")
  private String result;

  @PropertyDef(label = "开始时间")
  private Date createDate;
  @PropertyDef(label = "结束时间")
  private Date endDate;
  @PropertyDef(label = "操作人")
  private String operator;
  @PropertyDef(label = "处理节点")
  private String processBy;

  @Id
  @GeneratedValue
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "TASK_ID")
  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  @Column(name = "TYPE")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Column(name = "DESC_")
  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Column(name = "RESULT")
  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "END_DATE")
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Column(name = "OPERATOR")
  @Index(name = "IDX_OPERATOR")
  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  @Column(name = "PROCESS_BY")
  @Index(name = "IDX_PROCESS_BY")
  public String getProcessBy() {
    return processBy;
  }

  public void setProcessBy(String processBy) {
    this.processBy = processBy;
  }

  @Column(name = "PARAMETER")
  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  @Column(name = "BACKGROUND_TASK_BEANID")
  public String getBackgroundTaskBeanId() {
    return backgroundTaskBeanId;
  }

  public void setBackgroundTaskBeanId(String backgroundTaskBeanId) {
    this.backgroundTaskBeanId = backgroundTaskBeanId;
  }


}
