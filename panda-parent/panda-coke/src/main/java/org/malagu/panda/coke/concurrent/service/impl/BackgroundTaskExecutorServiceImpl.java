package org.malagu.panda.coke.concurrent.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import org.malagu.panda.coke.concurrent.domain.BackgroundTaskLog;
import org.malagu.panda.coke.concurrent.service.BackgroundTask;
import org.malagu.panda.coke.concurrent.service.BackgroundTaskExecutorService;
import org.malagu.panda.coke.concurrent.service.BackgroundTaskLogService;
import org.malagu.panda.coke.concurrent.service.BackgroundTaskRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.xobo.toolkit.JSONUtil;
import com.bstek.dorado.core.Configure;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@Service
public class BackgroundTaskExecutorServiceImpl
    implements BackgroundTaskExecutorService, ApplicationContextAware {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  class TaskInfo {

    public TaskInfo(ListenableFuture<Map<String, Object>> future, BackgroundTaskRunner runner) {
      this.future = future;
      this.runner = runner;
    }

    ListenableFuture<Map<String, Object>> future;
    BackgroundTaskRunner runner;
  }

  private ListeningExecutorService executorService;

  private Map<String, TaskInfo> backgroundTaskMap = new ConcurrentHashMap<String, TaskInfo>();

  private ApplicationContext applicationContext;

  @Autowired
  private BackgroundTaskLogService backgroundTaskService;

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.coke.concurrent.service.impl.
   * BackgroundTaskExecutorService#cancel(java.lang.String)
   */
  @Override
  public void cancel(String taskId) {

    TaskInfo taskInfo = backgroundTaskMap.get(taskId);
    if (taskInfo != null) {
      ListenableFuture<Map<String, Object>> listenableFuture = taskInfo.future;
      if (listenableFuture != null) {
        taskInfo.runner.cancelTask();
        if (listenableFuture.cancel(true)) {
          backgroundTaskService.markStatus(taskId, "C");
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.coke.concurrent.service.impl.
   * BackgroundTaskExecutorService#execute(java.lang.String, java.lang.String, java.lang.String,
   * java.util.Map)
   */
  @Override
  public void execute(String type, String desc, String backgroundTaskBeanId,
      Map<String, Object> parameter) {

    String taskId = backgroundTaskService.addBackgroundTask(type, desc, backgroundTaskBeanId,
        parameter, nodeName);
    execute(type, desc, backgroundTaskBeanId, parameter, taskId);
  }

  public void execute(String type, String desc, String backgroundTaskBeanId,
      Map<String, Object> parameter, final String taskId) {
    Object instance = applicationContext.getBean(backgroundTaskBeanId);

    BackgroundTask backgroundTask = null;
    if (instance instanceof BackgroundTask) {
      backgroundTask = (BackgroundTask) instance;
    }

    BackgroundTaskRunner backgroundTaskRunner = new BackgroundTaskRunner(backgroundTask, parameter);
    ListenableFuture<Map<String, Object>> listenableFuture =
        executorService.submit(backgroundTaskRunner);

    backgroundTaskMap.put(taskId, new TaskInfo(listenableFuture, backgroundTaskRunner));

    Futures.addCallback(listenableFuture, new FutureCallback<Map<String, Object>>() {

      @Override
      public void onSuccess(Map<String, Object> result) {
        backgroundTaskService.markSuccess(taskId, result);
        backgroundTaskMap.remove(taskId);
      }

      @Override
      public void onFailure(Throwable t) {
        logger.error("后台任务异常", t);
        backgroundTaskService.markFailure(taskId, t);
        backgroundTaskMap.remove(taskId);
      }
    });
  }

  @Override
  public void runAgain(String taskId) {
    BackgroundTaskLog backgroundTaskLog = backgroundTaskService.findBackgroundTaskLog(taskId);
    execute(backgroundTaskLog.getType(), backgroundTaskLog.getDesc(),
        backgroundTaskLog.getBackgroundTaskBeanId(),
        JSONUtil.toMap(backgroundTaskLog.getParameter()));
  }

  private String nodeName;

  // @PostConstruct
  public void init() {
    Long threadPoolSize = Configure.getLong("coke.threadPoolSize", 50);
    String nodeName = Configure.getString("coke.nodeName");
    executorService =
        MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threadPoolSize.intValue()));

    logger.info("start thread pool ({})", threadPoolSize);

    Collection<BackgroundTaskLog> tasks = backgroundTaskService.getBackgroundTaskLogs(nodeName);

    logger.info("resume {} tasks", tasks.size());

    for (BackgroundTaskLog backgroundTaskLog : tasks) {
      execute(backgroundTaskLog.getType(), backgroundTaskLog.getDesc(),
          backgroundTaskLog.getBackgroundTaskBeanId(),
          JSONUtil.toMap(backgroundTaskLog.getParameter()), backgroundTaskLog.getTaskId());
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
