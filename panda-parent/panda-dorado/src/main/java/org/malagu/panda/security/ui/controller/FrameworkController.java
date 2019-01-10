package org.malagu.panda.security.ui.controller;



import java.util.List;

import org.malagu.panda.security.orm.Url;
import org.malagu.panda.security.ui.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bstek.dorado.annotation.DataProvider;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
public class FrameworkController {
	
	@Autowired
	private FrameworkService frameworkService;
	
	@Value("${panda.loginSuccessPage}")
	private String loginSuccessPage;
	
	@RequestMapping("/") 
	public String home() {
		return "redirect:" + loginSuccessPage;
	}
	
	@DataProvider
	public List<Url> loadUrlForLoginUser() {
		return frameworkService.loadUrlForLoginUser();
	}

	
		

}
