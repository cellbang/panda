package org.malagu.panda.coke.concurrent.repository;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.malagu.panda.coke.concurrent.domain.BackgroundTaskLog;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BackgroundTaskRepository {

	public int updateResult(String taskId, String status, String result) {
		return JpaUtil.linu(BackgroundTaskLog.class).equal("taskId", taskId).set("status", status)
				.set("endDate", new Date()).set("result", result).update();
	}

	public void save(BackgroundTaskLog backgroundTaskLog) {
		JpaUtil.persist(backgroundTaskLog);
	}

	@Transactional(readOnly = true)
	public BackgroundTaskLog getByTaskId(String taskId) {
		return JpaUtil.linq(BackgroundTaskLog.class).equal("taskId", taskId).findOne();
	}

	@Transactional(readOnly = true)
	public Collection<BackgroundTaskLog> findProcessingTasksByProcessor(String processor) {
		DetachedCriteria dc = DetachedCriteria.forClass(BackgroundTaskLog.class);
		dc.add(Restrictions.eq("status", "P"));
		if (StringUtils.isNotEmpty(processor)) {
			dc.add(Restrictions.eq("processBy", processor));
		}

		return JpaUtil.linq(BackgroundTaskLog.class).equal("status", "P").addIf(processor).equal("processBy", processor)
				.endIf().list();
	}

}
