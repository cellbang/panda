package org.malagu.panda.coke.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;

public class CriteriaImplHelper {
  private static Method Method_getProjection = null;
  private static Method Method_getResultTransformer = null;

  private static Field Field_maxResults = null;
  private static Field Field_firstResult = null;
  private static Field Field_subcriteriaList = null;
  private static Field Field_orderEntries = null;
  private static Field Field_Order_propertyName = null;
  private static Field Field_Order_ignoreCase = null;
  private static Field DetachedCriteria_impl = null;

  private static Method Method_iterateSubcriteria = null;
  private static Method Method_iterateOrderings = null;
  private static Method Method_setProjection = null;
  private static Method Method_setResultTransformer = null;

  private static Method Method_Subcriteria_getAlias;
  private static Method Method_Subcriteria_getPath;
  private static Method Method_Subcriteria_getParent;

  private static Method Mehtod_OrderEntry_getCriteria;
  private static Method Method_OrderEntry_getOrder;

  private org.hibernate.Criteria criteriaImpl;
  private Map<String, String> aliasMap;

  public CriteriaImplHelper(DetachedCriteria detachedCriteria) {
    try {
      criteriaImpl = getCriteriaImpl(detachedCriteria);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public CriteriaImplHelper(org.hibernate.Criteria criteria) {
    criteriaImpl = criteria;
  }

  public void makeCount(org.hibernate.Criteria entityCriteria) throws Exception {
    // 查询记录条数
    Object countCriteria = entityCriteria;
    if (Method_iterateOrderings == null) {
      Method_iterateOrderings = countCriteria.getClass().getMethod("iterateOrderings");
    }
    Iterator<?> orderItr = (Iterator<?>) Method_iterateOrderings.invoke(countCriteria);
    if (orderItr != null) {
      while (orderItr.hasNext()) {
        orderItr.next();
        orderItr.remove();
      }
    }

    // countCriteria.setProjection(Projections.rowCount())
    if (Method_setProjection == null) {
      Method_setProjection = countCriteria.getClass().getMethod("setProjection");
    }
    Method_setProjection.invoke(countCriteria, Projections.rowCount());

    // countCriteria.setResultTransformer(Criteria.ROOT_ENTITY)
    if (Method_setResultTransformer == null) {
      Method_setResultTransformer = countCriteria.getClass().getMethod("setResultTransformer");
    }
    Method_setResultTransformer.invoke(countCriteria, Criteria.ROOT_ENTITY);

    clearMaxResults(countCriteria);
    clearFirstResult(countCriteria);
  }

  /**
   * 根据DataType中的propertyPath计算criteriaPath
   * 
   * @param propertyPath
   * @return
   */
  @SuppressWarnings("rawtypes")
  public String getCriteriaPath(String propertyPath) throws Exception {
    if (aliasMap == null) {
      aliasMap = new HashMap<String, String>();
      if (Method_iterateSubcriteria == null) {
        Method_iterateSubcriteria = criteriaImpl.getClass().getMethod("iterateSubcriteria");
      }

      if (Field_subcriteriaList == null) {
        Field f = criteriaImpl.getClass().getDeclaredField("subcriteriaList");
        f.setAccessible(true);
        Field_subcriteriaList = f;
      }

      List subcriteriaList = (List) Field_subcriteriaList.get(criteriaImpl);
      for (Object subcriteria : subcriteriaList) {
        if (Method_Subcriteria_getAlias == null) {
          Method_Subcriteria_getAlias = subcriteria.getClass().getMethod("getAlias");
        }

        String subAlias = (String) Method_Subcriteria_getAlias.invoke(subcriteria);
        if (StringUtils.isNotEmpty(subAlias)) {
          String fullPath = getFullPath(subcriteria);
          aliasMap.put(fullPath, subAlias);
        }
      }
    }

    if (propertyPath.indexOf('.') < 0) {
      if (aliasMap.containsKey(propertyPath)) {
        return aliasMap.get(propertyPath);
      } else {
        return propertyPath;
      }
    } else {
      int splitIndex = propertyPath.lastIndexOf('.');
      String parentPath = propertyPath.substring(0, splitIndex);
      String parentAlias = aliasMap.get(parentPath);
      if (StringUtils.isEmpty(parentAlias)) {
        return propertyPath;
      } else {
        return parentAlias + "." + propertyPath.substring(splitIndex + 1);
      }
    }
  }

  private String getFullPath(Object subcriteria) throws Exception {
    if (Method_Subcriteria_getPath == null) {
      Method_Subcriteria_getPath = subcriteria.getClass().getDeclaredMethod("getPath");
    }
    String subPath = (String) Method_Subcriteria_getPath.invoke(subcriteria);

    if (Method_Subcriteria_getParent == null) {
      Method_Subcriteria_getParent = subcriteria.getClass().getDeclaredMethod("getParent");
    }
    Criteria parentCriteria = (Criteria) Method_Subcriteria_getParent.invoke(subcriteria);
    if (parentCriteria.getClass().getSimpleName().equals("Subcriteria")) {
      return getFullPath(parentCriteria) + "." + subPath;
    } else {
      return subPath;
    }
  }

  /**
   * 合并给定的排序列表<br>
   * 
   * <code>规则：给定的排序列表优先级高，已经存在的排序优先级低</code>
   * 
   * @param hOrders
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  public void mergeOrders(List<org.hibernate.criterion.Order> hOrders) throws Exception {
    Map<String, org.hibernate.criterion.Order> oldOrderMap =
        new LinkedHashMap<String, org.hibernate.criterion.Order>();
    if (Field_orderEntries == null) {
      Field f = criteriaImpl.getClass().getDeclaredField("orderEntries");
      f.setAccessible(true);
      Field_orderEntries = f;
    }
    Iterator oldOrderEntryItr = ((List) Field_orderEntries.get(criteriaImpl)).iterator();
    while (oldOrderEntryItr.hasNext()) {
      Object orderEntry = oldOrderEntryItr.next();
      if (Mehtod_OrderEntry_getCriteria == null) {
        Mehtod_OrderEntry_getCriteria = orderEntry.getClass().getMethod("getCriteria");
      }

      org.hibernate.Criteria orderCriteria =
          (org.hibernate.Criteria) Mehtod_OrderEntry_getCriteria.invoke(orderEntry);
      if (criteriaImpl.equals(orderCriteria)) {
        if (Method_OrderEntry_getOrder == null) {
          Method_OrderEntry_getOrder = orderEntry.getClass().getDeclaredMethod("getOrder");
          org.hibernate.criterion.Order order =
              (org.hibernate.criterion.Order) Method_OrderEntry_getOrder.invoke(orderEntry);
          oldOrderMap.put(getPropertyName(order), order);
          oldOrderEntryItr.remove();
        }
      }
    }

    Set<String> newOrderNames = new HashSet<String>();
    for (org.hibernate.criterion.Order newOrder : hOrders) {
      criteriaImpl.addOrder(newOrder);
      String propertyName = getPropertyName(newOrder);

      if (oldOrderMap.containsKey(propertyName)) {
        org.hibernate.criterion.Order oldOrder = oldOrderMap.get(propertyName);
        if (isIgnoreCase(oldOrder)) {
          newOrder.ignoreCase();
        }
      }
      newOrderNames.add(getPropertyName(newOrder));
    }

    Set<String> oldOrderNames = oldOrderMap.keySet();
    for (String oldOrderName : oldOrderNames) {
      if (!newOrderNames.contains(oldOrderName)) {
        criteriaImpl.addOrder(oldOrderMap.get(oldOrderName));
      }
    }
  }

  @SuppressWarnings("rawtypes")
  public void setOrderEntries(List orderEntries) throws Exception {
    if (Field_orderEntries == null) {
      Field f = criteriaImpl.getClass().getDeclaredField("orderEntries");
      f.setAccessible(true);
      Field_orderEntries = f;
    }

    Field_orderEntries.set(criteriaImpl, orderEntries);
  }

  public Projection getProjection() throws Exception {
    if (Method_getProjection == null) {
      Method_getProjection = criteriaImpl.getClass().getMethod("getProjection");
    }

    return (Projection) Method_getProjection.invoke(criteriaImpl);
  }

  @SuppressWarnings("rawtypes")
  public List getOrderEntries() throws Exception {
    if (Field_orderEntries == null) {
      Field f = criteriaImpl.getClass().getDeclaredField("orderEntries");
      f.setAccessible(true);
      Field_orderEntries = f;
    }

    return (List) Field_orderEntries.get(criteriaImpl);
  }

  public ResultTransformer getResultTransformer() throws Exception {
    if (Method_getResultTransformer == null) {
      Method_getResultTransformer = criteriaImpl.getClass().getMethod("getResultTransformer");
    }

    return (ResultTransformer) Method_getResultTransformer.invoke(criteriaImpl);
  }

  public void setResultTransformer(ResultTransformer resultTransformer) {
    criteriaImpl.setResultTransformer(resultTransformer);
  }

  public void makeCount() throws Exception {
    // 查询记录条数
    org.hibernate.Criteria countCriteria = criteriaImpl;
    if (Method_iterateOrderings == null) {
      Method_iterateOrderings = countCriteria.getClass().getMethod("iterateOrderings");
    }
    Iterator<?> orderItr = (Iterator<?>) Method_iterateOrderings.invoke(countCriteria);
    if (orderItr != null) {
      while (orderItr.hasNext()) {
        orderItr.next();
        orderItr.remove();
      }
    }

    countCriteria.setProjection(Projections.rowCount());
    countCriteria.setResultTransformer(Criteria.ROOT_ENTITY);

    clearMaxResults(countCriteria);
    clearFirstResult(countCriteria);
  }

  public org.hibernate.Criteria getCriteria() {
    return criteriaImpl;
  }

  private boolean isIgnoreCase(org.hibernate.criterion.Order order) throws Exception {
    if (Field_Order_ignoreCase == null) {
      Field f = org.hibernate.criterion.Order.class.getDeclaredField("ignoreCase");
      f.setAccessible(true);
      Field_Order_ignoreCase = f;
    }

    return Field_Order_ignoreCase.getBoolean(order);
  }

  private void clearMaxResults(Object entityCriteria) throws Exception {
    if (Field_maxResults == null) {
      Field f = entityCriteria.getClass().getDeclaredField("maxResults");
      f.setAccessible(true);
      Field_maxResults = f;
    }
    Field_maxResults.set(entityCriteria, null);
  }

  private void clearFirstResult(Object entityCriteria) throws Exception {
    if (Field_firstResult == null) {
      Field f = entityCriteria.getClass().getDeclaredField("firstResult");
      f.setAccessible(true);
      Field_firstResult = f;
    }
    Field_firstResult.set(entityCriteria, null);
  }

  private String getPropertyName(org.hibernate.criterion.Order order) throws Exception {
    if (Field_Order_propertyName == null) {
      Field f = org.hibernate.criterion.Order.class.getDeclaredField("propertyName");
      f.setAccessible(true);
      Field_Order_propertyName = f;
    }

    return (String) Field_Order_propertyName.get(order);
  }

  private org.hibernate.Criteria getCriteriaImpl(DetachedCriteria detachedCriteria)
      throws Exception {
    if (DetachedCriteria_impl == null) {
      Field f = DetachedCriteria.class.getDeclaredField("impl");
      f.setAccessible(true);
      DetachedCriteria_impl = f;
    }

    return (org.hibernate.Criteria) DetachedCriteria_impl.get(detachedCriteria);
  }

}
