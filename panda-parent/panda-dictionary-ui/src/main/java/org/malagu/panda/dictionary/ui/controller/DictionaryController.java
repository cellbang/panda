package org.malagu.panda.dictionary.ui.controller;

import java.util.List;
import org.malagu.panda.dictionary.domain.Dictionary;
import org.malagu.panda.dictionary.domain.DictionaryItem;
import org.malagu.panda.dictionary.ui.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年2月7日
 */
@Controller
public class DictionaryController {

  @Autowired
  private DictionaryService dictionaryUIService;

  @Autowired
  private org.malagu.panda.dictionary.service.DictionaryService dictionaryService;

  @DataProvider
  public void load(Page<Dictionary> page) {
    dictionaryUIService.load(page);
  }

  @DataProvider
  public List<Dictionary> loadChildren(String parentId) {
    return dictionaryUIService.loadChildren(parentId);
  }

  @DataProvider
  public List<DictionaryItem> loadDictionaryItems(String dictionaryId) {
    return dictionaryUIService.loadDictionaryItems(dictionaryId);
  }

  @DataProvider
  public List<DictionaryItem> loadDictionaryItemsByCode(String code) {
    return dictionaryService.getDictionaryItemsBy(code);
  }

  @DataProvider
  public List<DictionaryItem> loadDictionaryItemChildren(String parentId) {
    return dictionaryUIService.loadDictionItemChildren(parentId);
  }

  @Expose
  public String isExists(String code) {
    if (dictionaryUIService.isExists(code)) {
      return "编码已存在。";
    }
    return null;
  }

  @DataResolver
  public void save(List<Dictionary> dictionaries) {
    dictionaryUIService.save(dictionaries);

  }
}
