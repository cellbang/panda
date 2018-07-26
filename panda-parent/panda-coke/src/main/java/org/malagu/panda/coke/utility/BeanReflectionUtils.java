package org.malagu.panda.coke.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;
import javassist.util.proxy.ProxyObject;
import net.sf.cglib.proxy.Proxy;

public class BeanReflectionUtils {

  /**
   * 加载Clazz及其父类的Fields
   * 
   * @param clazz
   * @return
   */
  public static Collection<Field> loadClassFields(Class<?> clazz) {
    Collection<Field> fields = new ArrayList<Field>();
    Class<?> currentClazz = clazz;
    while (!currentClazz.equals(Object.class)) {
      fields.addAll(Arrays.asList(currentClazz.getDeclaredFields()));
      currentClazz = currentClazz.getSuperclass();
    }
    return fields;
  }

  public static Class<?> getClass(Object instance) {
    Class<?> clazz = instance.getClass();
    if (instance instanceof ProxyObject) {
      clazz = clazz.getSuperclass();
    } else if (Proxy.isProxyClass(clazz)) {
      return clazz.getSuperclass();
    } else if (AopUtils.isJdkDynamicProxy(clazz)) {
      return clazz.getSuperclass();
    } else if (ClassUtils.isCglibProxyClass(clazz)) {
      return clazz.getSuperclass();
    }
    return clazz;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Map<String, Object> entityToMap(Object value) {
    Map<String, Object> entity = new LinkedHashMap<String, Object>();
    if (value instanceof Map) {
      entity.putAll((Map<? extends String, ? extends Object>) value);
    } else if (value instanceof Collection) {
      Integer i = 0;
      for (Object obj : (Collection<?>) value) {
        entity.put(i.toString(), entityToMap(obj));
        i++;
      }
    } else if (value != null) {
      Map beanmap = new BeanMap(value);
      entity.putAll(beanmap);
    }
    return entity;
  }

  public static void mergeObjectWithAppointedProperties(Object target, Object source,
      String[] properties) {
    for (String property : properties) {
      try {
        Object targetValue = PropertyUtils.getProperty(target, property);
        Object sourceValue = PropertyUtils.getProperty(source, property);
        if (targetValue == null && sourceValue != null) {
          PropertyUtils.setProperty(target, property, sourceValue);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
