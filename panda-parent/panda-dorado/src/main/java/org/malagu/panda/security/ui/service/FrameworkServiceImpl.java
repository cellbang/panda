package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年1月2日
 */
@Service
@Transactional(readOnly = true)
public class FrameworkServiceImpl implements FrameworkService {

	@Autowired
	private UrlService urlService;
	
	@Override
	public List<Url> loadUrlForLoginUser() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return urlService.findTreeByUsername(user.getUsername());
	}

}
