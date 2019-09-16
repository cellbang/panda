package org.malagu.panda.coke.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.service.PojoMappingConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.toolkit.BeanReflectionUtil;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataTypeSupport;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.property.Mapping;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.google.common.collect.Maps;

@Service
public class PojoMappingConvertServiceImpl implements PojoMappingConvertService {

  @Autowired
  private DataTypeManager dataTypeManager;

  public Map<String, String> convertValue(Object data) {
    Class<?> clazz = BeanReflectionUtil.getClass(data);
    DataType dataType = null;
    try {
      dataType = dataTypeManager.getDataType(clazz);
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    if (dataType == null) {
      return null;
    }

    Map<String, Map<String, String>> dataTypeMappingMap = extractDataType(dataType);
    if (dataTypeMappingMap == null) {
      return null;
    }

    Map<String, String> convertedValue =
        Maps.newLinkedHashMapWithExpectedSize(dataTypeMappingMap.size());

    dataTypeMappingMap.forEach((name, mapping) -> {
      try {
        String value = BeanUtils.getProperty(data, name);
        if (StringUtils.isNotEmpty(value)) {
          convertedValue.put(name, mapping.get(value));
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    });

    return convertedValue;

  }

  public Map<String, Map<String, String>> extractDataType(DataType dataType) {
    EntityDataTypeSupport defaultEntityDataType = (EntityDataTypeSupport) dataType;
    Map<String, PropertyDef> propertyDefMap = defaultEntityDataType.getPropertyDefs();

    Map<String, Map<String, String>> dataTypeMapping =
        Maps.newLinkedHashMapWithExpectedSize(propertyDefMap.size());

    propertyDefMap.forEach((name, propertyDef) -> {
      Map<String, String> mappings = extractPropertyDefMappings(propertyDef);
      if (mappings != null) {
        dataTypeMapping.put(name, mappings);
      }
    });

    return dataTypeMapping;

  }

  @SuppressWarnings("unchecked")
  public Map<String, String> extractPropertyDefMappings(PropertyDef propertyDef) {
    Mapping mapping = propertyDef.getMapping();

    if (mapping == null) {
      return null;
    }

    Object mapValues = mapping.getMapValues();

    if (mapValues == null) {
      return null;
    }

    String keyProperty = StringUtils.defaultIfBlank(mapping.getKeyProperty(), "key");
    String valueProperty = StringUtils.defaultIfBlank(mapping.getValueProperty(), "value");

    Map<String, String> mappings = Maps.newLinkedHashMap();
    if (mapValues instanceof Collection<?>) {
      for (Object mapValue : (Collection<?>) mapValues) {
        putMappings(mappings, mapValue, keyProperty, valueProperty);
      }
    } else if (mapValues.getClass().isArray()) {
      Object[] mapValuesArr = (Object[]) mapValues;
      for (Object object : mapValuesArr) {
        putMappings(mappings, object, keyProperty, valueProperty);
      }
    } else if (mapValues instanceof Map) {
      mappings.putAll((Map<? extends String, ? extends String>) mapValues);
    } else {
      putMappings(mappings, mapValues, keyProperty, valueProperty);
    }
    return mappings;
  }


  public void putMappings(Map<String, String> mappings, Object mapValue, String keyProperty,
      String valueProperty) {
    String key;
    String value;
    try {
      key = BeanUtils.getProperty(mapValue, keyProperty);
      value = BeanUtils.getProperty(mapValue, valueProperty);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    if (!StringUtils.isAnyEmpty(key, value)) {
      mappings.put(key, value);
    }
  }


}
