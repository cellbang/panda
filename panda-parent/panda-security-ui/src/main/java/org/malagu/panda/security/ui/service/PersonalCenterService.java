package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.orm.Role;
import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.orm.User;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年1月9日
 */
public interface PersonalCenterService {

	User getUser(String username);

	List<Role> getRoles(String username);

	List<Url> getUrls(String username);

	List<Permission> getPermissions(String username);

}
