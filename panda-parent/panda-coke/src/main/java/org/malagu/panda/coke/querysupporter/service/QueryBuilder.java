package org.malagu.panda.coke.querysupporter.service;

import java.util.Map;
import org.malagu.panda.coke.querysupporter.model.QueryResolver;
import com.bstek.dorado.data.provider.Criteria;

public interface QueryBuilder {
  public static final String BEAN_ID = "coke.queryBuilder";

  QueryResolver extractQuery(Class<?> clazz, Criteria criteria);

  QueryResolver extractQuery(Class<?> clazz, Criteria criteria, String alias);

  QueryResolver extractQuery(Class<?> clazz, Criteria criteria, Map<String, Object> queryParameter);

  QueryResolver extractQuery(Class<?> clazz, Criteria criteria, Map<String, Object> queryParameter,
      String alias);

}
