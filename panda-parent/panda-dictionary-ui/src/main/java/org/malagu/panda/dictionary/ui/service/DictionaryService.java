package org.malagu.panda.dictionary.ui.service;

import java.util.List;

import org.malagu.panda.dictionary.domain.Dictionary;
import org.malagu.panda.dictionary.domain.DictionaryItem;

import com.bstek.dorado.data.provider.Page;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年2月7日
 */
public interface DictionaryService {
	
	void load(Page<Dictionary> page);
	
	List<Dictionary> loadChildren(String parentId);
	
	List<DictionaryItem> loadDictionaryItems(String dictionaryId);
	
	List<DictionaryItem> loadDictionItemChildren(String parentId);
	
	boolean isExists(String code);
	
	void save(List<Dictionary> dictionaries);

}
