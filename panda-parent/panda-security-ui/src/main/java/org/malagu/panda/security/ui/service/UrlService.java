package org.malagu.panda.security.ui.service;

import java.util.List;
import java.util.Set;

import org.malagu.panda.security.orm.Url;



/**
 * 菜单服务接口
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月30日
 */
public interface UrlService {
	
	List<Url> load();
	
	void save(List<Url> urls);
	
	/**
	 * 获取所有菜单（只有菜单信息）
	 * @return
	 * @author sr on 2019-01-23 
	 */
	List<Url> findAll();
	
	/**
     * 校验用户是否有访问菜单的权限
     * @param urls
     * @param userName
     * @return
     * @author sr on 2019-01-23 
     */
    Boolean checkUrlAuthority(Set<String> urls, String userName);

}
