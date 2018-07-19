package org.malagu.panda.security.access.provider.impl;

import java.util.Collection;
import java.util.List;

import org.malagu.linq.JpaUtil;
import org.malagu.panda.security.access.provider.GrantedAuthorityProvider;
import org.malagu.panda.security.orm.RoleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 默认用户授权信息提供者
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年2月27日
 */
@Component
public class UserGrantedAuthorityProvider implements GrantedAuthorityProvider {

	@Override
	public Collection<? extends GrantedAuthority> provide(
			UserDetails userDetails) {
		List<GrantedAuthority> authorities = JpaUtil.linq(RoleGrantedAuthority.class).equal("actorId", userDetails.getUsername()).list();
		return authorities;
	}

}
