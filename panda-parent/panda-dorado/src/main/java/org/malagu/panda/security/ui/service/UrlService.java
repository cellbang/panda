package org.malagu.panda.security.ui.service;

import java.util.List;

import org.malagu.panda.security.orm.Url;



/**
 * 菜单服务接口
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月30日
 */
public interface UrlService {
	
	List<Url> load();
	
	void save(List<Url> urls);

}
