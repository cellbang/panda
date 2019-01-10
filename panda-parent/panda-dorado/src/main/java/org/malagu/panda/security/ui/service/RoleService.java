package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Role;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月12日
 */
public interface RoleService {

	void load(Page<Role> page, Criteria criteria);

	void save(List<Role> roles);

}
