package org.malagu.panda.coke.querysupporter.service;

import java.util.List;
import java.util.Map;

import org.malagu.panda.coke.querysupporter.model.PropertyWrapper;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;

/**
 * 把paramter转化为dorado criteria
 * 
 * @author bing
 * 
 */
public interface DoradoCriteriaBuilder {
  public static final String BEAN_ID = "coke.doradoCriteriaBuilder";

  /**
   * @param queryParameter
   * @param propertyOperatorMap
   * @param criteria
   * @param entityClass
   * @return
   */
  Criteria mergeQueryParameterCriteria(Map<String, Object> queryParameter,
      Map<String, PropertyWrapper> propertyOperatorMap, Criteria criteria, Class<?> entityClass);

  /**
   * @param queryParameter
   * @param propertyOperatorMap
   * @param entityClass
   * @return
   */
  List<Criterion> extractQueryParameter(Map<String, Object> queryParameter,
      Map<String, PropertyWrapper> propertyOperatorMap, Class<?> entityClass);

}
