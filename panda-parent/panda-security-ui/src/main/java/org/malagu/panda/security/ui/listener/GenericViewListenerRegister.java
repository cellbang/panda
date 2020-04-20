package org.malagu.panda.security.ui.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.dorado.data.config.definition.GenericObjectListenerRegister;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月9日
 */
@Component
public class GenericViewListenerRegister extends GenericObjectListenerRegister {

	@Autowired
	private GenericViewListener listener;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		listener.setOrder(order);
		listener.setPattern("*");
		this.setListener(listener);
		super.afterPropertiesSet();
	}

	@Value("${panda.security.viewlistener.order}")
	private Integer order;
		
}
