package org.malagu.panda.security.ui.service;

import java.util.Collection;
import java.util.List;

import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.ui.builder.ViewComponent;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月12日
 */
public interface PermissionService {

	Collection<ViewComponent> loadComponents(String viewName);

	List<Permission> loadPermissions(String roleId, String urlId);

	void save(Permission permission);

}
