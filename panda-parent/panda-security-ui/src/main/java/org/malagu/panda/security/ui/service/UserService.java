package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.User;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年1月2日
 */
public interface UserService {

	void load(Page<User> page, Criteria criteria);

	String validateOldPassword(String oldPassword);

	void save(List<User> users);
	
	void save(User user);

	void changePassword(String username, String newPassword);

	boolean isExist(String username);

}
