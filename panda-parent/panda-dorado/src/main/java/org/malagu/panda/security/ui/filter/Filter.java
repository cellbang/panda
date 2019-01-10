package org.malagu.panda.security.ui.filter;

import com.bstek.dorado.view.AbstractViewElement;



public interface Filter<T extends AbstractViewElement>{
		
	void invoke(T control);
	
	boolean support(Object control);
}
