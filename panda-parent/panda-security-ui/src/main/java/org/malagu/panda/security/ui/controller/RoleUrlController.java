package org.malagu.panda.security.ui.controller;



import java.util.List;

import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.ui.service.RoleUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
public class RoleUrlController {
	
	@Autowired
	private RoleUrlService roleUrlService;
	
	@DataProvider
	public List<Permission> load(String roleId) {
		return roleUrlService.load(roleId);
	}
	
	@DataResolver
	public void save(List<Permission> permissions) {
		roleUrlService.save(permissions);
	}
	

}
