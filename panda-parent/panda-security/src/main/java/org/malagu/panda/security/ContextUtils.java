package org.malagu.panda.security;

import org.malagu.panda.security.orm.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年8月10日
 */
public abstract class ContextUtils {
	
	public static User getLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof User) {
			return (User) authentication.getPrincipal();
			
		}
		return null;
	}
	
	public static String getLoginUsername() {
		User user = getLoginUser();
		if (user != null) {
			return user.getUsername();
		}
		return null;
	}
}
