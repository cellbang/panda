package org.malagu.panda.security.ui.controller;



import java.util.List;

import org.malagu.panda.security.orm.Role;
import org.malagu.panda.security.ui.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@DataProvider
	public void load(Page<Role>page, Criteria criteria) {
		roleService.load(page, criteria);
	}
	
	@DataResolver
	public void save(List<Role> roles) {
		roleService.save(roles);
	}
	

}
