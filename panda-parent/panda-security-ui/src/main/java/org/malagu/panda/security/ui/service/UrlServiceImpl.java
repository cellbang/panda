package org.malagu.panda.security.ui.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.policy.SaveContext;
import org.malagu.panda.dorado.linq.policy.impl.SmartSavePolicyAdapter;
import org.malagu.panda.security.cache.SecurityCacheEvict;
import org.malagu.panda.security.orm.Permission;
import org.malagu.panda.security.orm.Url;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月30日
 */
@Service("ui.urlService")
@Transactional(readOnly = true)
public class UrlServiceImpl implements UrlService {
	
	@Override
	public List<Url> load() {
		List<Url> result = new ArrayList<Url>();
		Map<String, List<Url>> childrenMap = new HashMap<String, List<Url>>();
		List<Url> urls = JpaUtil
				.linq(Url.class)
				.asc("order")
				.list();
		for (Url url : urls) {
			
			if (childrenMap.containsKey(url.getId())) {
				url.setChildren(childrenMap.get(url.getId()));
			} else {
				url.setChildren(new ArrayList<Url>());
				childrenMap.put(url.getId(), url.getChildren());
			}

			if (url.getParentId() == null) {
				result.add(url);
			} else {
				List<Url> children;
				if (childrenMap.containsKey(url.getParentId())) {
					children = childrenMap.get(url.getParentId());
				} else {
					children = new ArrayList<Url>();
					childrenMap.put(url.getParentId(), children);
				}
				children.add(url);
			}
		}
		return result;
	}

	@Override
	@SecurityCacheEvict
	@Transactional
	public void save(List<Url> urls) {
		JpaUtil.save(urls, new SmartSavePolicyAdapter() {
			
			@Override
			public boolean beforeDelete(SaveContext context) {
				Url url = context.getEntity();
				JpaUtil.lind(Permission.class)
					.equal("resourceId", url.getId())
					.equal("resourceType", Url.RESOURCE_TYPE)
					.delete();
				return true;
			}

			@Override
			public void apply(SaveContext context) {
				Url url = context.getEntity();
				if (url.getParentId() == null) {
					Url parent = context.getParent();
					if (parent != null) {
						url.setParentId(parent.getId());
					}
				}
				super.apply(context);
			}	
		});
	}



}
