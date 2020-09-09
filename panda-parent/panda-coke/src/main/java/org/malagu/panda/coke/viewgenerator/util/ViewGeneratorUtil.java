package org.malagu.panda.coke.viewgenerator.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.viewgenerator.domain.EntityDef;
import org.malagu.panda.coke.viewgenerator.domain.Property;
import org.reflections.Reflections;
import org.xobo.toolkit.BeanReflectionUtil;
import com.bstek.dorado.annotation.PropertyDef;
import com.google.common.collect.ImmutableSet;

public class ViewGeneratorUtil {
  public static EntityDef build(Class<?> clazz) {
    return build(clazz, Collections.emptySet());
  }

  public static EntityDef build(Class<?> clazz, Set<String> queryPropNameSet) {
    List<Field> filedList = BeanReflectionUtil.loadClassFields(clazz);

    EntityDef entityDef = new EntityDef();
    entityDef.setName(clazz.getSimpleName());
    entityDef.setClazz(clazz.getName());
    entityDef.setDataSet("dataSet" + clazz.getSimpleName());

    List<Property> propertyList = new ArrayList<Property>(filedList.size());
    List<Property> queryPropertyList = new ArrayList<Property>(filedList.size());
    entityDef.setProps(propertyList);

    for (Field field : filedList) {
      if (Modifier.isStatic(field.getModifiers())) {
        continue;
      }

      PropertyDef propertyDef = field.getAnnotation(PropertyDef.class);

      Class<?> type = field.getType();
      String typeSimpleName = type.getSimpleName();

      if (!SUPPORT_DATATYPE_SET.contains(typeSimpleName)) {
        continue;
      }
      System.out.println(type.getSimpleName());

      Property property = new Property();
      propertyList.add(property);

      property.setName(field.getName());
      if (propertyDef != null) {
        property.setLabel(propertyDef.label());
        property.setDescription(propertyDef.description());
      }

      if (queryPropNameSet.contains(property)) {
        queryPropertyList.add(property);
      }
    }

    return entityDef;

  }

  public static void main(String[] args) {

    EntityDef master = build(EntityDef.class);
    EntityDef slave = build(Property.class);

    setEntitySlave(master, slave, "#.children");

    Map<String, Object> dataModel = new HashMap<>();
    dataModel.put("entity", master);
    dataModel.put("master", master);
    dataModel.put("slave", slave);
    slave.setParameter("parentId=$${this.id}");

    master.getChildren().add(slave);
    String result = FreeMarkerUtil.generate("MasterSlaveView.view.ftl", dataModel);
    System.out.println(result);

    result = FreeMarkerUtil.generate("SinglePageMaintain.java.ftl", dataModel);
    System.out.println(result);
  }

  public static void setEntitySlave(EntityDef master, EntityDef slave, String listDataPath) {
    slave.setMaster(false);
    slave.setDataSet(master.getDataSet());

    if (StringUtils.isEmpty(listDataPath)) {
      return;
    }

    int childrenStart = listDataPath.lastIndexOf('.');
    String children = listDataPath.substring(childrenStart + 1, listDataPath.length());
    String dataPathPrefix = listDataPath.substring(0, childrenStart + 1);

    slave.setCurrentDataPath(dataPathPrefix + "#" + children);
    slave.setListDataPath(listDataPath);
    slave.setListName(children);
    slave.setParentName(master.getName());

  }

  public static Collection<EntityDef> importEntityDef(String packagePath) {

    Reflections reflections = new Reflections(packagePath);
    Set<Class<? extends Serializable>> allTypes = reflections.getSubTypesOf(Serializable.class);

    Collection<EntityDef> entityDefs = new ArrayList<EntityDef>(allTypes.size());
    for (Class<? extends Serializable> clazz : allTypes) {
      if (Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      entityDefs.add(build(clazz));
    }
    return entityDefs;
  }

  private static Set<String> SUPPORT_DATATYPE_SET = ImmutableSet.of("Float", "BigDecimal", "long", "Integer", "byte",
      "Short", "Time", "boolean", "DateTime", "short", "Date", "char", "Boolean", "float", "String", "Calendar",
      "Double", "Byte", "double", "int", "Long", "Character");
}
