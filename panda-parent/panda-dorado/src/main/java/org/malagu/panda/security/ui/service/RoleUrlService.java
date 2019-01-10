package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Permission;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月12日
 */
public interface RoleUrlService {

	List<Permission> load(String roleId);

	void save(List<Permission> permissions);

}
