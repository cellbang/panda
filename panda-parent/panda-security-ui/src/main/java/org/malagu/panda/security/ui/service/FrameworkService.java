package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Url;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年1月2日
 */
public interface FrameworkService {
	
	List<Url> loadUrlForLoginUser();
}
