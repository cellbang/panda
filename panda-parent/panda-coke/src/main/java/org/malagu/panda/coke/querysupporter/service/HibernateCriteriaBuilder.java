package org.malagu.panda.coke.querysupporter.service;

import org.hibernate.criterion.DetachedCriteria;

import com.bstek.dorado.data.provider.Criteria;

public interface HibernateCriteriaBuilder {
  public static final String BEAN_ID = "coke.hibernateCriteriaBuilder";

  DetachedCriteria buildDetachedCriteria(Criteria criteria, Class<?> entityClass);

  DetachedCriteria buildDetachedCriteria(Criteria criteria, Class<?> entityClass, String alias);

}
