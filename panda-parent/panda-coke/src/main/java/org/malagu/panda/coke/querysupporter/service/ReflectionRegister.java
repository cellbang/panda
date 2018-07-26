package org.malagu.panda.coke.querysupporter.service;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * @author bing
 * 
 */
public interface ReflectionRegister {
	void register(Class<?> clazz, Field field);

	void register(Class<?> clazz, String name, AccessibleObject accessibleObject, String columnName);
}
