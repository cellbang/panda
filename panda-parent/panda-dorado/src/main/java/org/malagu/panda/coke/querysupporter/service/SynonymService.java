package org.malagu.panda.coke.querysupporter.service;

import java.util.Collection;

/**
 * 记录Hibernate实体上，标注Synonym及Pinyin注解的属性。
 * 
 * @author bing
 * 
 */
public interface SynonymService {
  static final String BEAN_ID = "coke.synonymService";

  /**
   * @param clazz 实体
   * @param property 属性
   * @return 返回实体属性，所对应的等价属性集合。
   */
  Collection<String> find(Class<?> clazz, String property);
}
