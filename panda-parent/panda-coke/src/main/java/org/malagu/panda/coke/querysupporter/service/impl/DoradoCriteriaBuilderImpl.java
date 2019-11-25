package org.malagu.panda.coke.querysupporter.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Resource;
import org.malagu.panda.coke.querysupporter.model.OrMap;
import org.malagu.panda.coke.querysupporter.model.PropertyWrapper;
import org.malagu.panda.coke.querysupporter.service.DoradoCriteriaBuilder;
import org.malagu.panda.coke.querysupporter.service.QueryPropertyWrapperService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Or;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.type.StringDataType;

@Service(DoradoCriteriaBuilderImpl.BEAN_ID)
public class DoradoCriteriaBuilderImpl implements DoradoCriteriaBuilder {
  public static final String BEAN_ID = "coke.doradoCriteriaBuilder";

  @Resource(name = QueryPropertyWrapperService.BEAN_ID)
  private QueryPropertyWrapperService propertyWrapperService;

  @Override
  public Criteria mergeQueryParameterCriteria(Class<?> entityClass,
      Map<String, Object> queryParameter, Criteria criteria,
      Map<String, PropertyWrapper> propertyOperatorMap) {
    if (criteria == null) {
      criteria = new Criteria();
    }
    criteria.getCriterions()
        .addAll(extractQueryParameter(entityClass, queryParameter, propertyOperatorMap));
    return criteria;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Criterion> extractQueryParameter(Class<?> entityClass,
      Map<String, Object> queryParameter, Map<String, PropertyWrapper> propertyOperatorMap) {

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

      Criterion newCriterion = null;
      if (value instanceof OrMap) {
        Or or = new Or();
        List<Criterion> orCriterions =
            extractQueryParameter(entityClass, ((OrMap) value).getConditions(), propertyOperatorMap);
        or.setCriterions(orCriterions);
        newCriterion = or;
      } else {
        PropertyWrapper propertyWrapper =
            propertyWrapperService.find(entityClass, property, propertyOperatorMap);

        if (propertyWrapper == null) {
          continue;
        }

        SingleValueFilterCriterion criterion = new SingleValueFilterCriterion();
        criterion.setProperty(propertyWrapper.getProperty());
        criterion.setFilterOperator(propertyWrapper.getFilterOperator());
        if (FilterOperator.in == propertyWrapper.getFilterOperator()) {
          Collection<Object> collection = null;
          if (value instanceof Collection) {
            collection = (Collection<Object>) value;
          } else {
            String[] str = Objects.toString(value, "").split(",");
            collection = Arrays.asList(str);
          }

          Collection<Object> list = new HashSet<>();
          for (Object obj : collection) {
            list.add(propertyWrapper.parseValue(obj));
          }
          value = list;
          // propertyWrapper.setDataType(new StringDataType());
        } else {
          value = propertyWrapper.parseValue(value);
        }

        if (value instanceof Date) {
          if (FilterOperator.le == propertyWrapper.getFilterOperator()) {
            value = getLastMillisecond((Date) value);
          }
        }
        criterion.setValue(value);
        newCriterion = criterion;
      }
      criterions.add(newCriterion);
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

  public static Date getTomorrowDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    cal.add(Calendar.MILLISECOND, -1);
    return cal.getTime();
  }

  public static Date getLastMillisecond(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    cal.add(Calendar.MILLISECOND, -1);
    return cal.getTime();
  }

  public static void main(String[] args) {
    System.out.println(getLastMillisecond(new Date()));
  }

  @Override
  public Criteria mergeQueryParameterCriteria(Class<?> entityClass,
      Map<String, Object> queryParameter, Criteria criteria) {
    return mergeQueryParameterCriteria(entityClass, queryParameter, criteria, null);
  }

  @Override
  public List<Criterion> extractQueryParameter(Class<?> entityClass,
      Map<String, Object> queryParameter) {
    return extractQueryParameter(entityClass, queryParameter, null);
  }
}
