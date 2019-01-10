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
public class SmartSavePolicyAdapter implements SavePolicy {

	@Override
	public void apply(SaveContext context) {
		Object entity = context.getEntity();
		EntityManager entityManager = context.getEntityManager();
		EntityState state = EntityUtils.getState(entity);
		if (EntityState.NEW.equals(state)) {
			if (beforeInsert(context)) {
				try {
					entityManager.persist(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				afterInsert(context);
			}
		} else if (EntityState.MODIFIED.equals(state) 
				|| EntityState.MOVED.equals(state)) {
			if (beforeUpdate(context)) {
				try {
					entityManager.merge(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				afterUpdate(context);
			}
		} else if (EntityState.DELETED.equals(state)) {
			if (beforeDelete(context)) {
				try {
					entityManager.remove(entityManager.merge(entity));
				} catch (Exception e) {
					e.printStackTrace();
				}
				afterDelete(context);
			}
			
		}
	}
	
	public boolean beforeDelete(SaveContext context) {
		return true;
	}
	
	public void afterDelete(SaveContext context) {
		
	}
	
	public boolean beforeInsert(SaveContext context) {
		return true;
	}
	
	public void afterInsert(SaveContext context) {
		
	}
	
	public boolean beforeUpdate(SaveContext context) {
		return true;
	}
	
	public void afterUpdate(SaveContext context) {
		
	}
	

}
