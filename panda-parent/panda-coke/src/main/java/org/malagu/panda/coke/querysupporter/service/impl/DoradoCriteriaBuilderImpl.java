package org.malagu.panda.coke.querysupporter.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.malagu.panda.coke.querysupporter.model.CokeFilterCriterion;
import org.malagu.panda.coke.querysupporter.model.PropertyWrapper;
import org.malagu.panda.coke.querysupporter.service.DoradoCriteriaBuilder;
import org.malagu.panda.coke.querysupporter.service.QueryPropertyWrapperService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.filter.FilterOperator;

@Service(DoradoCriteriaBuilderImpl.BEAN_ID)
public class DoradoCriteriaBuilderImpl implements DoradoCriteriaBuilder {
	public static final String BEAN_ID = "coke.parameterToDcriteria";

	@Resource(name = QueryPropertyWrapperService.BEAN_ID)
	private QueryPropertyWrapperService propertyWrapperService;

	@Override
	public Criteria mergeQueryParameterCriteria(Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap, Criteria criteria, Class<?> entityClass) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		criteria.getCriterions().addAll(extractQueryParameter(queryParameter, propertyOperatorMap, entityClass));
		return criteria;
	}

	@Override
	public List<Criterion> extractQueryParameter(Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap, Class<?> entityClass) {

		List<Criterion> criterions = new ArrayList<Criterion>();
		if (queryParameter == null) {
			return criterions;
		}

		for (Entry<String, Object> entry : queryParameter.entrySet()) {
			String property = entry.getKey();
			Object value = entry.getValue();

			if (StringUtils.isEmpty(value)) {
				continue;
			}

			PropertyWrapper propertyWrapper = propertyWrapperService.find(entityClass, property, propertyOperatorMap);

			if (propertyWrapper == null) {
				continue;
			}

			CokeFilterCriterion criterion = new CokeFilterCriterion();
			criterion.setProperty(propertyWrapper.getProperty());
			criterion.setFilterOperator(propertyWrapper.getFilterOperator());
			value = propertyWrapper.parseValue(value);

			if (value instanceof Date) {
				if (FilterOperator.le == propertyWrapper.getFilterOperator()) {
					value = getTomorrowDate((Date) value);
					criterion.setFilterOperator(FilterOperator.lt);
				}
			}
			criterion.setValue(value);
			criterions.add(criterion);
		}
		return criterions;
	}

	public String parseQueryKeyword(String queryWord) {
		int length = queryWord.length();
		StringBuilder wordBuilder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = queryWord.charAt(i);
			switch (c) {
			case '*':
				wordBuilder.append("%");
				break;
			case '?':
				wordBuilder.append("_");
				break;
			default:
				wordBuilder.append(c);
			}
		}
		return wordBuilder.toString();
	}

	private Date getTomorrowDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
