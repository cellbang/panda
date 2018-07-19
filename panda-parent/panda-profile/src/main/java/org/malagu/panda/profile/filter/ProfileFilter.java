package org.malagu.panda.profile.filter;

import org.malagu.panda.profile.domain.ComponentConfig;

import com.bstek.dorado.view.ViewElement;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年7月17日
 */
public interface ProfileFilter<T extends ViewElement> {
	
	void apply(T t, ComponentConfig config);
	
	boolean support(ViewElement viewElement);

}
