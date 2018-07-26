package org.malagu.panda.coke.querysupporter.service.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.malagu.panda.coke.dataType.MapMap;
import org.malagu.panda.coke.model.UnkonwDataType;
import org.malagu.panda.coke.querysupporter.model.EnumDataType;
import org.malagu.panda.coke.querysupporter.model.PropertyWrapper;
import org.malagu.panda.coke.querysupporter.service.QueryPropertyWrapperService;
import org.malagu.panda.coke.querysupporter.service.ReflectionRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;

@Service(QueryPropertyWrapperService.BEAN_ID)
public class QueryPreLoadPropertyWrapperServiceImpl implements QueryPropertyWrapperService, ReflectionRegister {

	private static final Logger logger = LoggerFactory.getLogger(HibernateEntityEnhancerImpl.class);

	private static MapMap<Class<?>, String, PropertyWrapper> golbalPropertyWrapperMap = MapMap.concurrentHashMap();

	@Autowired
	private DataTypeManager dataTypeManager;

	@Override
	public Map<String, PropertyWrapper> findClassPropertyWrapper(Class<?> clazz) {
		return golbalPropertyWrapperMap.get(clazz);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PropertyWrapper find(Class<?> clazz, String property, Map<String, PropertyWrapper> propertyWrapperMap) {

		PropertyWrapper propertyWrapper = null;
		if (propertyWrapperMap != null) {
			propertyWrapper = propertyWrapperMap.get(property);
			if (propertyWrapper != null) {
				propertyWrapper.setAutoCreate(false);
			}
		}

		if (propertyWrapper == null) {
			propertyWrapper = golbalPropertyWrapperMap.get(clazz, property);
			if (propertyWrapper != null) {
				propertyWrapper.setAutoCreate(true);
			}
		}

		if (propertyWrapper != null) {
			DataType dataType = propertyWrapper.getDataType();
			if (dataType == null) {
				Class<?> propertyTypeclazz = propertyWrapper.getType();
				if (Enum.class.isAssignableFrom(propertyTypeclazz)) {
					dataType = new EnumDataType((Class<? extends Enum>) propertyTypeclazz);
				} else {
					dataType = extractPropertyType(propertyWrapper.getType(), propertyWrapper.getProperty());
				}

				if (dataType == null) {
					dataType = new UnkonwDataType();
				}
				propertyWrapper.setDataType(dataType);
			}
		}
		return propertyWrapper;
	}

	private FilterOperator getFilterOperator(String operator) {
		if ("eq".equalsIgnoreCase(operator)) {
			return FilterOperator.eq;
		} else if ("ge".equalsIgnoreCase(operator)) {
			return FilterOperator.ge;
		} else if ("gt".equalsIgnoreCase(operator)) {
			return FilterOperator.gt;
		} else if ("in".equalsIgnoreCase(operator)) {
			return FilterOperator.in;
		} else if ("le".equalsIgnoreCase(operator)) {
			return FilterOperator.le;
		} else if ("like".equalsIgnoreCase(operator)) {
			return FilterOperator.like;
		} else if ("likeEnd".equalsIgnoreCase(operator)) {
			return FilterOperator.likeEnd;
		} else if ("lt".equalsIgnoreCase(operator)) {
			return FilterOperator.lt;
		} else if ("ne".equalsIgnoreCase(operator)) {
			return FilterOperator.ne;
		} else if ("between".equalsIgnoreCase(operator)) {
			return FilterOperator.between;
		} else {
			return FilterOperator.eq;
		}
	}

	private DataType extractPropertyType(Class<?> type, String property) {
		DataType propertyDataType = null;
		try {
			propertyDataType = dataTypeManager.getDataType(type);
		} catch (Exception e) {
			logger.info("Could not find datatype for {}", type);
		}
		return propertyDataType;
	}

	public static final String[] QUERY_OPERATOR = new String[] { /* "Eq", */"Ge", "Gt", "In", "Le", "Like", "LikeEnd",
			"Lt", "Ne", "Between" };

	@Override
	public void register(Class<?> clazz, Field field) {
		Map<String, PropertyWrapper> classPropertyWrapper = golbalPropertyWrapperMap.safeGet(clazz);
		String property = field.getName();
		Class<?> type = field.getType();
		if (!type.equals(Object.class)) {
			classPropertyWrapper.put(property, new PropertyWrapper(property, type, FilterOperator.eq));
			for (String operator : QUERY_OPERATOR) {
				String newProperty = property + operator;
				classPropertyWrapper.put(newProperty, new PropertyWrapper(property, type, getFilterOperator(operator)));
			}
		}
	}

	@Override
	public void register(Class<?> clazz, String name, AccessibleObject accessibleObject, String columnName) {
		Map<String, PropertyWrapper> classPropertyWrapper = golbalPropertyWrapperMap.safeGet(clazz);

		Class<?> type = null;
		if (accessibleObject instanceof Field) {
			type = ((Field) accessibleObject).getType();
		} else if (accessibleObject instanceof Method) {
			type = ((Method) accessibleObject).getReturnType();
		}
		if (!type.equals(Object.class)) {
			classPropertyWrapper.put(name, new PropertyWrapper(name, type, FilterOperator.eq));
			for (String operator : QUERY_OPERATOR) {
				String newProperty = name + operator;
				classPropertyWrapper.put(newProperty, new PropertyWrapper(name, type, getFilterOperator(operator)));
			}
		}

	}

}
