package org.malagu.panda.security.ui.controller;



import java.util.Arrays;
import java.util.List;

import org.malagu.panda.security.orm.User;
import org.malagu.panda.security.ui.service.PersonalCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;



/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Controller
public class PersonalCenterController {
	
	@Autowired
	protected PersonalCenterService personalCenterService;
	
	@DataProvider
	public List<User> loadUser(String username) {
		return Arrays.asList(personalCenterService.getUser(username));
	}

}
