package org.malagu.panda.security.decision.manager.impl;

import java.util.Collection;
import java.util.List;
import org.malagu.panda.security.decision.manager.SecurityDecisionManager;
import org.malagu.panda.security.orm.Resource;
import org.malagu.panda.security.orm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月7日
 */
@Service
public class SecurityDecisionManagerImpl implements SecurityDecisionManager {

  @Autowired
  private List<SecurityMetadataSource> securityMetadataSources;

  @Autowired
  private AccessDecisionManager accessDecisionManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  public boolean decide(Resource resource) {
    return decide(null, resource);
  }

  @Override
  public boolean decide(String username, Resource resource) {
    if (resource != null) {
      Collection<ConfigAttribute> attributes = findConfigAttributes(resource);
      Authentication authentication = getAuthentication(username);
      User user = (User) authentication.getPrincipal();
      if (user.isAdministrator()) {
        return true;
      }
      try {
        accessDecisionManager.decide(authentication, resource, attributes);
      } catch (AccessDeniedException e) {
        return false;
      } catch (InsufficientAuthenticationException e) {
        return false;
      }

    }
    return true;
  }

  protected Authentication getAuthentication(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (username == null) {
      return authentication;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails
        && username.equals(((UserDetails) principal).getUsername())) {
      return authentication;
    }

    UserDetails user = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(user, null);
  }

  @Override
  public Collection<ConfigAttribute> findConfigAttributes(Resource resource) {
    Collection<ConfigAttribute> attributes = resource.getAttributes();
    if (CollectionUtils.isEmpty(attributes)) {
      for (SecurityMetadataSource securityMetadataSource : securityMetadataSources) {
        if (securityMetadataSource.supports(resource.getClass())) {
          attributes = securityMetadataSource.getAttributes(resource);
        }
      }
    }
    return attributes;
  }



}
