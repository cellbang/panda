package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.policy.SaveContext;
import org.malagu.panda.dorado.linq.policy.SavePolicy;
import org.malagu.panda.dorado.linq.policy.impl.SmartSavePolicyAdapter;
import org.malagu.panda.security.cache.SecurityCacheEvict;
import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.orm.Role;
import org.malagu.panda.security.orm.RoleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月12日
 */
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

	private SavePolicy roleSavePolicy = new RoleSavePolicy();
	
	@Override
	public void load(Page<Role>page, Criteria criteria) {
		JpaUtil.linq(Role.class).where(criteria).paging(page);
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional
	public void save(List<Role> roles) {
		JpaUtil.save(roles, roleSavePolicy);
	}
	
	class RoleSavePolicy extends SmartSavePolicyAdapter {

		@Override
		public boolean beforeDelete(SaveContext context) {
			if (context.getEntity() instanceof Role) {
				Role role = context.getEntity();
				JpaUtil
					.lind(Permission.class)
					.equal("roleId", role.getId())
					.delete();
				
				JpaUtil
					.lind(RoleGrantedAuthority.class)
					.equal("roleId", role.getId())
					.delete();
			}
			return true;
		}

	}
}
