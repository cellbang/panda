package org.malagu.panda.dorado.linq.policy.impl;

import javax.persistence.EntityManager;

import org.malagu.panda.dorado.linq.policy.SaveContext;
import org.malagu.panda.dorado.linq.policy.SavePolicy;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;


/**
 *@author Kevin.yang
 *@since 2015年5月17日
 */
public class SmartSavePolicy implements SavePolicy {

	@Override
	public void apply(SaveContext context) {
		Object entity = context.getEntity();
		EntityManager entityManager = context.getEntityManager();
		EntityState state = EntityUtils.getState(entity);
		if (EntityState.NEW.equals(state)) {
			try {
				entityManager.persist(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (EntityState.MODIFIED.equals(state) 
				|| EntityState.MOVED.equals(state)) {
			try {
				entityManager.merge(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (EntityState.DELETED.equals(state)) {
			try {
				entityManager.remove(entityManager.merge(entity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
