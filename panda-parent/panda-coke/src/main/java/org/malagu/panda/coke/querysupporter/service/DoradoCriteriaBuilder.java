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
	 * @param entityClass
	 * @param queryParameter
	 * @param criteria
	 * @param propertyOperatorMap
	 * @return
	 */
	Criteria mergeQueryParameterCriteria(Class<?> entityClass, Map<String, Object> queryParameter, Criteria criteria);

	/**
	 * @param entityClass
	 * @param queryParameter
	 * @param propertyOperatorMap
	 * @return
	 */
	List<Criterion> extractQueryParameter(Class<?> entityClass, Map<String, Object> queryParameter);

	/**
	 * @param entityClass
	 * @param queryParameter
	 * @param criteria
	 * @param propertyOperatorMap
	 * @return
	 */
	Criteria mergeQueryParameterCriteria(Class<?> entityClass, Map<String, Object> queryParameter, Criteria criteria,
			Map<String, PropertyWrapper> propertyOperatorMap);

	/**
	 * @param entityClass
	 * @param queryParameter
	 * @param propertyOperatorMap
	 * @return
	 */
	List<Criterion> extractQueryParameter(Class<?> entityClass, Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap);

}
