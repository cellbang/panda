package org.malagu.panda.dictionary.ui.el;

import java.util.List;

import org.malagu.panda.dictionary.domain.DictionaryItem;
import org.malagu.panda.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 字典表达式辅助对象
 * 
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年2月8日
 */
@Component
public class DictionaryExpressionObject {

	@Autowired
	private DictionaryService dictionaryService;
	
    @Cacheable(cacheNames = "test")
	public List<DictionaryItem> items(String code) {		
		return dictionaryService.getDictionaryItemsBy(code);
	}
	
	public String defaultValue(String code) {		
		return dictionaryService.getDefaultValueBy(code);
	}
	
	public String defaultKey(String code) {		
		return dictionaryService.getDefaultKeyBy(code);
	}
	
	public DictionaryItem defaultValueItem(String code) {		
		return dictionaryService.getDefaultValueItemBy(code);
	}
	
	public DictionaryItem item(String key) {
		return dictionaryService.getDictionaryItem(key);
	}
	
	
}
