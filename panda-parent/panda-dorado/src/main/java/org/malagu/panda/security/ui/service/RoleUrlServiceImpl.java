package org.malagu.panda.security.ui.service;

import java.util.List;
import java.util.Set;

import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.lin.Linq;
import org.malagu.panda.dorado.linq.policy.SaveContext;
import org.malagu.panda.dorado.linq.policy.SavePolicy;
import org.malagu.panda.dorado.linq.policy.impl.SmartSavePolicyAdapter;
import org.malagu.panda.security.cache.SecurityCacheEvict;
import org.malagu.panda.security.orm.Component;
import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.orm.Url;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月12日
 */
@Service
@Transactional(readOnly = true)
public class RoleUrlServiceImpl implements RoleUrlService {

	private SavePolicy permissionSavePolicy = new PermissionSavePolicy();

	
	@Override
	public List<Permission> load(String roleId) {
		return JpaUtil
				.linq(Permission.class)
				.toEntity()
				.equal("roleId", roleId)
				.equal("resourceType", Url.RESOURCE_TYPE)
				.collect(Url.class, "resourceId")
				.list();
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional
	public void save(List<Permission> permissions) {
		JpaUtil.save(permissions, permissionSavePolicy);
	}
	
	class PermissionSavePolicy extends SmartSavePolicyAdapter {

		@Override
		public boolean beforeDelete(SaveContext context) {
			Permission permission = context.getEntity();
			Linq linq = JpaUtil.linq(Permission.class);
			linq
				.collect("resourceId")
				.equal("resourceType", Component.RESOURCE_TYPE)
				.exists(Component.class)
					.equalProperty("id", "resourceId")
					.equal("urlId", permission.getResourceId())
				.list();
			Set<String> ids = linq.getLinqContext().getSet("resourceId");
			if (ids != null) {
				JpaUtil
					.lind(Permission.class)
					.in("resourceId", ids)
					.delete();
				JpaUtil
					.lind(Component.class)
					.in("id", ids)
					.delete();
			}
			return true;
		}	

	}

}
