package org.malagu.panda.coke.querysupporter.service.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.coke.dataType.MapMap;
import org.malagu.panda.coke.querysupporter.annotations.Pinyin;
import org.malagu.panda.coke.querysupporter.annotations.Synonym;
import org.malagu.panda.coke.querysupporter.service.ReflectionRegister;
import org.malagu.panda.coke.querysupporter.service.SynonymService;
import org.springframework.stereotype.Service;

@Service(SynonymService.BEAN_ID)
public class SynonymServiceImpl implements SynonymService, ReflectionRegister {
  private static final MapMap<Class<?>, String, Collection<String>> synonymPropertyMap =
      MapMap.concurrentHashMap();

  @Override
  public Collection<String> find(Class<?> clazz, String property) {
    Collection<String> unionPropertyList = synonymPropertyMap.get(clazz, property);
    if (unionPropertyList == null) {
      return Collections.emptyList();
    } else {
      return unionPropertyList;
    }
  }

  @Override
  public void register(Class<?> clazz, Field field) {
    Synonym union = field.getAnnotation(Synonym.class);
    Pinyin pinyin = field.getAnnotation(Pinyin.class);
    Collection<String> properties = new HashSet<String>();

    String property = field.getName();
    if (union != null) {
      String value = union.value();
      properties.addAll(Arrays.asList(value.split(",")));
    }

    if (pinyin != null) {
      String jian = pinyin.jian();
      String quan = pinyin.quan();

      jian = StringUtils.isEmpty(jian) ? property + "Jian" : jian;
      quan = StringUtils.isEmpty(quan) ? property + "Quan" : quan;

      properties.add(jian);
      properties.add(quan);

      properties.add(property);
    }

    if (!properties.isEmpty()) {
      synonymPropertyMap.add(clazz, property, properties);
    }
  }

  public static MapMap<Class<?>, String, Collection<String>> getPinyinmap() {
    return synonymPropertyMap;
  }

  @Override
  public void register(Class<?> clazz, String name, AccessibleObject accessibleObject,
      String columnName) {
    Synonym union = null;
    Pinyin pinyin = null;
    if (accessibleObject instanceof Field) {
      Field field = (Field) accessibleObject;
      union = field.getAnnotation(Synonym.class);
      pinyin = field.getAnnotation(Pinyin.class);

    } else if (accessibleObject instanceof Method) {
      Method field = (Method) accessibleObject;
      union = field.getAnnotation(Synonym.class);
      pinyin = field.getAnnotation(Pinyin.class);

    }
    Collection<String> properties = new HashSet<String>();

    String property = name;
    if (union != null) {
      String value = union.value();
      properties.addAll(Arrays.asList(value.split(",")));
    }

    if (pinyin != null) {
      String jian = pinyin.jian();
      String quan = pinyin.quan();

      jian = StringUtils.isEmpty(jian) ? property + "Jian" : jian;
      quan = StringUtils.isEmpty(quan) ? property + "Quan" : quan;

      properties.add(jian);
      properties.add(quan);

      properties.add(property);
    }

    if (!properties.isEmpty()) {
      synonymPropertyMap.add(clazz, property, properties);
    }
  }

}
