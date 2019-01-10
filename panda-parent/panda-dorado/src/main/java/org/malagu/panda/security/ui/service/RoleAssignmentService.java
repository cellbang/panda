package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Role;
import org.malagu.panda.security.orm.User;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年1月2日
 */
public interface RoleAssignmentService {

	void loadUsersWithout(Page<User> page, Criteria criteria, String roleId);

	void loadUsersWithin(Page<User> page, Criteria criteria, String roleId);

	void save(List<Role> roles);

}
