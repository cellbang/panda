package org.malagu.panda.notice.ui.strategy;

import org.malagu.panda.notice.strategy.CategoryProvider;
import org.springframework.stereotype.Component;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月13日
 */
@Component
public class StaticCategoryProvider implements CategoryProvider {

	@Override
	public String provide() {
		return "bdf3";
	}

}
