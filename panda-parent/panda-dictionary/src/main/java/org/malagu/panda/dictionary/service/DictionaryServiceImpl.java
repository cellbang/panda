package org.malagu.panda.dictionary.service;

import java.util.List;
import java.util.Map;
import org.malagu.linq.JpaUtil;
import org.malagu.panda.dictionary.domain.Dictionary;
import org.malagu.panda.dictionary.domain.DictionaryItem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年2月7日
 */
@Service
@Transactional(readOnly = true)
public class DictionaryServiceImpl implements DictionaryService {

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public Dictionary getDictionaryBy(String code) {
    return JpaUtil.linq(Dictionary.class).equal("code", code).findOne();
  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public DictionaryItem getDefaultValueItemBy(String code) {

    return JpaUtil.linq(DictionaryItem.class).isTrue("enabled").exists(Dictionary.class).equal("code", code)
        .equalProperty("defaultValue", "key").equalProperty("id", "dictionaryId").end().findOne();
  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public String getDefaultValueBy(String code) {
    return getDefaultValueItemBy(code).getValue();
  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public String getDefaultKeyBy(String code) {
    return getDefaultValueItemBy(code).getKey();
  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public List<DictionaryItem> getDictionaryItemsBy(String code) {
    List<DictionaryItem> list = JpaUtil.linq(DictionaryItem.class).isTrue("enabled").exists(Dictionary.class)
        .equal("code", code).equalProperty("id", "dictionaryId").isNull("parentId").end().asc("order").list();
    Map<String, List<DictionaryItem>> map = JpaUtil.classify(list, "parentId");
    List<DictionaryItem> top = map.get(null);
    if (top != null) {
      for (DictionaryItem item : top) {
        item.setChildren(map.get(item.getId()));
      }
    }

    return top;
  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public List<DictionaryItem> getAllDictionaryItemsBy(String code) {
    return JpaUtil.linq(DictionaryItem.class).isTrue("enabled").exists(Dictionary.class).equal("code", code)
        .equalProperty("id", "dictionaryId").end().asc("order").list();

  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public DictionaryItem getDictionaryItem(String key) {
    return JpaUtil.linq(DictionaryItem.class).isTrue("enabled").equal("key", key).findOne();
  }

  @Override
  @Cacheable(value = CACHE_KEY, key = "methodName + ':' + #p0")
  public List<DictionaryItem> getDictionaryItemsBy(String[] codes) {
    List<DictionaryItem> list = JpaUtil.linq(DictionaryItem.class).isTrue("enabled").exists(Dictionary.class)
        .in("code", (Object[]) codes).equalProperty("id", "dictionaryId").end().asc("order").list();
    Map<String, List<DictionaryItem>> map = JpaUtil.classify(list, "parentId");
    List<DictionaryItem> top = map.get(null);
    if (top != null) {
      for (DictionaryItem item : top) {
        item.setChildren(map.get(item.getId()));
      }
    }

    return top;
  }

}
