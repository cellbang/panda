package org.malagu.panda.log.view;



import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.log.model.LogInfo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
@Transactional(readOnly = true)
public class LogInfoController {
	
	@DataProvider
	public void load(Page<LogInfo> page, Criteria criteria) {
		JpaUtil.linq(LogInfo.class).where(criteria).desc("operationDate").paging(page);
	}
	

}
