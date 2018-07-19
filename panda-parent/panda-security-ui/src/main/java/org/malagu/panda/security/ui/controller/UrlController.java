package org.malagu.panda.security.ui.controller;



import java.util.List;

import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.ui.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@DataProvider
	public List<Url> load() {
		return urlService.load();
	}
		
	@DataResolver
	public void save(List<Url> urls) {
		urlService.save(urls);
	}
	

}
