package org.malagu.panda.coke.concurrent.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.malagu.panda.coke.api.model.Result;
import org.malagu.panda.coke.concurrent.domain.BackgroundTaskLog;
import org.malagu.panda.coke.concurrent.repository.BackgroundTaskRepository;
import org.malagu.panda.coke.concurrent.service.BackgroundTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.toolkit.JSONUtil;

@Service
public class BackgroundTaskLogServiceImpl implements BackgroundTaskLogService {


  @Override
  public void markSuccess(String taskId, Map<String, Object> result) {
    Result resultInfo = Result.success();
    resultInfo.setData(result);
    backgroundTaskRepository.updateResult(taskId, "S", JSONUtil.toJSON(resultInfo));
  }

  @Override
  public void markFailure(String taskId, Throwable t) {
    String messsage = ExceptionUtils.getRootCauseMessage(t);
    Result resultInfo = Result.fail("1000", messsage);
    backgroundTaskRepository.updateResult(taskId, "F", JSONUtil.toJSON(resultInfo));
  }

  @Override
  public String addBackgroundTask(String type, String desc, String backgroundTaskBeanId,
      Map<String, Object> parameter, String nodeName) {
    BackgroundTaskLog backgroundTaskLog = new BackgroundTaskLog();
    String taskId = UUID.randomUUID().toString();
    backgroundTaskLog.setTaskId(taskId);
    backgroundTaskLog.setType(type);
    backgroundTaskLog.setDesc(desc);
    backgroundTaskLog.setBackgroundTaskBeanId(backgroundTaskBeanId);
    String json = JSONUtil.toJSON(parameter);
    backgroundTaskLog.setParameter(json);
    backgroundTaskLog.setStatus("P");
    backgroundTaskLog.setCreateDate(new Date());
    backgroundTaskLog.setProcessBy(nodeName);
    String userId = MapUtils.getString(parameter, "userId");
    backgroundTaskLog.setOperator(userId);
    backgroundTaskRepository.save(backgroundTaskLog);
    return taskId;
  }

  @Autowired
  private BackgroundTaskRepository backgroundTaskRepository;

  @Override
  public void markStatus(String taskId, String status) {
    backgroundTaskRepository.updateResult(taskId, status, null);
  }

  @Override
  public BackgroundTaskLog findBackgroundTaskLog(String taskId) {
    return backgroundTaskRepository.getByTaskId(taskId);
  }

  @Override
  public Collection<BackgroundTaskLog> getBackgroundTaskLogs(String processor) {
    return backgroundTaskRepository.findProcessingTasksByProcessor(processor);
  }

}
