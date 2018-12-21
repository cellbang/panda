package org.malagu.panda.coke.concurrent.view;

import java.util.Map;
import org.malagu.panda.coke.concurrent.domain.BackgroundTaskLog;
import org.malagu.panda.coke.concurrent.service.BackgroundTaskExecutorService;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Controller
public class BackgroundTaskMaintain {

  @DataProvider
  public void loadBackgroundTaskLogs(Page<BackgroundTaskLog> page, Criteria criteria,
      Map<String, Object> parameter) {
    JpaUtil.linq(BackgroundTaskLog.class).where(criteria).paging(page);
  }

  @Expose
  public void addBackgroundTask(String type, String desc, String backgroundTaskBeanId,
      Map<String, Object> parameter) {
    backgroundTaskExecutorService.execute(type, desc, backgroundTaskBeanId, parameter);
  }

  @Expose
  public void operation(String operation, String taskId) {
    if ("runAgain".equals(operation)) {
      backgroundTaskExecutorService.runAgain(taskId);
    } else if ("cancel".equals(operation)) {
      backgroundTaskExecutorService.cancel(taskId);
    }

  }

  @Autowired
  private BackgroundTaskExecutorService backgroundTaskExecutorService;

}
