package org.malagu.panda.autoconfigure.multitenant;

import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.strategy.CurrentOrganizationStrategy;
import org.malagu.panda.security.ContextUtils;
import org.malagu.panda.security.orm.User;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年12月14日
 */
public class CurrentOrganizationStrategyImpl implements CurrentOrganizationStrategy {

	@Override
	public Organization getCurrent() {
		User user = ContextUtils.getLoginUser();
		if (user != null) {
			return user.getOrganization();
		}
		return null;
	}

}
