package org.malagu.panda.security.ui.controller;



import java.util.Collection;
import java.util.List;

import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.ui.builder.ViewComponent;
import org.malagu.panda.security.ui.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
public class PermissionController {
	
	
	@Autowired
	private PermissionService permissionService;
	
	@DataProvider
	public Collection<ViewComponent> loadComponents(String viewName) throws Exception {
		return permissionService.loadComponents(viewName);
	}
	
	
	@DataProvider
	public List<Permission> loadPermissions(String roleId, String urlId) {
		return permissionService.loadPermissions(roleId, urlId);
	}
	
	@DataResolver
	public void save(Permission permission) {
		permissionService.save(permission);
	}

	
	
	

}
