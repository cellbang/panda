package org.malagu.panda.dorado.linq.initiator;

import org.malagu.linq.initiator.JpaUtilInitiator;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.policy.CriteriaPolicy;
import org.malagu.panda.dorado.linq.policy.SavePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年7月3日
 */
public class DoradoJpaUtilInitiator implements JpaUtilInitiator {

	@Autowired
	private CriteriaPolicy defaultQBCCriteriaPolicy;
	
	@Autowired
	private SavePolicy defaultSavePolicy;
	

	@Override
	public void initialize(ApplicationContext applicationContext) {
		JpaUtil.setDefaultQBCCriteriaPolicy(defaultQBCCriteriaPolicy);
		JpaUtil.setDefaultSavePolicy(defaultSavePolicy);
	}

}
