package org.malagu.panda.dorado.linq.configure;

import org.malagu.panda.dorado.linq.initiator.DoradoJpaUtilInitiator;
import org.malagu.panda.dorado.linq.policy.CriteriaPolicy;
import org.malagu.panda.dorado.linq.policy.SavePolicy;
import org.malagu.panda.dorado.linq.policy.impl.DirtyTreeSavePolicy;
import org.malagu.panda.dorado.linq.policy.impl.QBCCriteriaPolicy;
import org.malagu.panda.dorado.linq.policy.impl.SmartSavePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@Configuration
public class JpaConfiguration {
	
	
	@Bean
	public CriteriaPolicy defaultQBCCriteriaPolicy() {
		return new QBCCriteriaPolicy();
	}
	
	@Bean
	public SavePolicy defaultSavePolicy() {
		DirtyTreeSavePolicy savePolicy = new DirtyTreeSavePolicy();
		savePolicy.setSavePolicy(new SmartSavePolicy());
		return savePolicy;
	}
	
	@Bean
	public DoradoJpaUtilInitiator doradoJpaUtilInitiator() {
		return new DoradoJpaUtilInitiator();
	}


}
