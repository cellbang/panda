package org.malagu.panda.log.proxy;

import org.malagu.panda.log.annotation.Log;
import org.malagu.panda.log.proxy.LogProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年1月11日
 */
@Component
@Log(module = "系统模块", category = "系统日志")
public class DataResolverLogProxy implements LogProxy {

	@Value("${panda.log.disabled}")
	private boolean disabled;
	
	@Value("${panda.log.defaultLog}")
	private boolean defaultLog;

	
	@Override
	public boolean support(Object obj) {
		return !disabled && defaultLog;
	}

}
