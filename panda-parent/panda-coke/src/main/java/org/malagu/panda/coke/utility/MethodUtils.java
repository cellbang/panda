package org.malagu.panda.coke.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class MethodUtils {
	private static Paranamer paranamer = new CachingParanamer(new BytecodeReadingParanamer());

	private static Map<Method, String[]> parameterNamesMap = new ConcurrentHashMap<Method, String[]>();

	/**
	 * 反射调用指定方法
	 * 
	 * @param target
	 * @param methodName
	 * @param parameter
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	public static Object invokeMethod(Object target, String methodName, Map<String, Object> parameter)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
			NoSuchMethodException, InstantiationException {
		Class<?>[] paramTypes = null;
		Method method = ReflectionUtils.findMethod(BeanReflectionUtils.getClass(target), methodName, paramTypes);
		return invokeMethod(target, method, parameter);
	}

	public static Object invokeMethod(Object target, String methodName, JsonNode rootNode)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
			NoSuchMethodException, InstantiationException, JsonParseException, JsonMappingException, IOException {
		Class<?>[] paramTypes = null;
		Method method = ReflectionUtils.findMethod(BeanReflectionUtils.getClass(target), methodName, paramTypes);
		return invokeMethod(target, method, rootNode);
	}

	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object target, Method method, Map<String, Object> parameter)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		String[] parameterNames = parameterNamesMap.get(method);
		if (parameterNames == null) {
			parameterNames = paranamer.lookupParameterNames(method);
			if (parameterNames == null) {
				parameterNames = new String[] {};
			}
			parameterNamesMap.put(method, parameterNames);
		}
		Type[] parametersType = method.getGenericParameterTypes();
		Object[] realArgs = new Object[parametersType.length];
		for (int i = 0; i < parameterNames.length; i++) {
			String name = parameterNames[i];
			Object value;
			if (parameterNames.length == 1) {
				value = parameter;
			} else {
				value = parameter.get(name);
			}
			if (value != null) {
				Type ptype = parametersType[i];
				if (ptype instanceof Class<?>) {
					Class<?> clazz = (Class<?>) ptype;
					if (!clazz.isAssignableFrom(value.getClass())) {
						value = ConvertUtils.convert(value, clazz);
					}
					if (!clazz.isPrimitive() && !clazz.isAssignableFrom(value.getClass())
							&& !clazz.getPackage().getName().startsWith("java") && value instanceof Map) {
						Object instance = ClassUtils.createInstance(clazz, (Map<String, Object>) value);
						value = instance;
					}
					if (Collection.class.isAssignableFrom(value.getClass())) {
						Collection<Object> newValues = new ArrayList<Object>();
						for (Object object : (Collection<Object>) value) {
							if (object instanceof Map) {
								// Object entity = clazzType.newInstance();
								// BeanUtils.populate(entity, (Map<String, ?
								// extends Object>) object);
								// newValues.add(entity);
							}
						}
						value = newValues;
					}
				} else if (ptype instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) ptype;
					Type[] typeArguments = parameterizedType.getActualTypeArguments();
					Type rawType = parameterizedType.getRawType();
					Collection<Object> collection = new ArrayList<Object>();
					if (rawType instanceof Class<?> && Collection.class.isAssignableFrom((Class<?>) rawType)) {
						if (typeArguments.length > 0) {
							Type typeArgument = typeArguments[0];
							Class<?> rt = (Class<?>) typeArgument;

							for (Object object : (Collection<Object>) value) {
								Object entity = rt.newInstance();
								BeanUtils.populate(entity, (Map<String, ? extends Object>) object);
								collection.add(entity);
							}
						}
						value = collection;
					}
				}
			}
			realArgs[i] = value;
		}
		return method.invoke(target, realArgs);
	}

	public static Object invokeMethod(Object target, Method method, JsonNode rootNode)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException,
			JsonParseException, JsonMappingException, IOException {
		String[] parameterNames = parameterNamesMap.get(method);
		if (parameterNames == null) {
			parameterNames = paranamer.lookupParameterNames(method);
			if (parameterNames == null) {
				parameterNames = new String[] {};
			}
			parameterNamesMap.put(method, parameterNames);
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		Type[] parametersType = method.getGenericParameterTypes();
		Object[] realArgs = new Object[parametersType.length];

		if (rootNode != null) {
			for (int i = 0; i < parameterNames.length; i++) {
				String name = parameterNames[i];
				JsonNode valueNode = rootNode.get(name);

				if (valueNode == null && parameterNames.length == 1 && rootNode.size() > 0) {
					valueNode = rootNode;
				}

				Object value = convertJsonNodeToValueOfTargetType(mapper, valueNode, parametersType[i]);

				realArgs[i] = value;
			}
		}
		return method.invoke(target, realArgs);
	}

	public static Object convertJsonNodeToValueOfTargetType(ObjectMapper mapper, JsonNode valueNode, Type type)
			throws JsonParseException, JsonMappingException, IOException {
		if (valueNode == null) {
			return null;
		}

		Object value = null;
		if (type instanceof Class<?>) {
			value = mapper.readValue(valueNode.toString(), (Class<?>) type);
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type[] typeArguments = parameterizedType.getActualTypeArguments();
			Type rawType = parameterizedType.getRawType();
			if (typeArguments.length > 0) {
				Type typeArgument = typeArguments[0];
				Class<?> rt;
				if (Class.class.isAssignableFrom(typeArgument.getClass())) {
					rt = (Class<?>) typeArgument;
				} else {
					rt = Object.class;
				}
				if (rawType instanceof Class<?>) {
					if (Collection.class.isAssignableFrom((Class<?>) rawType)) {
						if (typeArguments.length > 0) {
							JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class,
									List.class, rt);
							value = mapper.readValue(valueNode.toString(), javaType);
						}
					} else if (Map.class.isAssignableFrom((Class<?>) rawType)) {
						JavaType javaType = mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class,
								Map.class, rt, Object.class);
						value = mapper.readValue(valueNode.toString(), javaType);
					} else {
						JavaType javaType = mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class,
								Map.class, rt, Object.class);
						Map<String, Object> result = mapper.readValue(valueNode.toString(), javaType);
						value = ClassUtils.createInstance(rawType, result);
					}
				}

			}
		}
		return value;
	}
}
