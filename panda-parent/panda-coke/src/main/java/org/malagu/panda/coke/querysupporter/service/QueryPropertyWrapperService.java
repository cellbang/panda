package org.malagu.panda.coke.querysupporter.service;

import java.util.Map;

import org.malagu.panda.coke.querysupporter.model.PropertyWrapper;

public interface QueryPropertyWrapperService {
  static final String BEAN_ID = "coke.queryPropertyWrapperLoaderService";
  public static final String PROPERTY_OPERATOR_SPLITER = "@";

  PropertyWrapper find(Class<?> clazz, String property,
      Map<String, PropertyWrapper> propertyWrapperMap);

	Map<String, PropertyWrapper> findClassPropertyWrapper(Class<?> clazz);
}
