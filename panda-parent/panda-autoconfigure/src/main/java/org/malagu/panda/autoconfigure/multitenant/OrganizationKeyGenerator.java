package org.malagu.panda.autoconfigure.multitenant;

import java.lang.reflect.Method;

import org.malagu.multitenant.domain.Organization;
import org.malagu.panda.security.orm.OrganizationSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年8月14日
 */
public class OrganizationKeyGenerator implements KeyGenerator {
	
	@Override
	public Object generate(Object target, Method method, Object... params) {
		String organizationId = "";
		if (SecurityContextHolder.getContext() != null 
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof OrganizationSupport) {
				Organization organization = ((OrganizationSupport) principal).getOrganization();
				if (organization != null) {
					organizationId = organization.getId();
				}
			}
		}
		return organizationId;
	}

}
