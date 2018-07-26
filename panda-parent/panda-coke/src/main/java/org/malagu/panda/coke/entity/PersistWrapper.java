package org.malagu.panda.coke.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.malagu.panda.coke.dataType.ListMap;
import org.malagu.panda.coke.service.PersistAction;
import org.malagu.panda.coke.service.impl.NopPersistAction;
import javassist.util.proxy.ProxyObject;

public class PersistWrapper {
  private ListMap<Class<?>, ReferenceWrapper> referenceWrapperMap = ListMap.concurrentHashMap();
  private Map<Class<?>, PersistAction<?>> persistActionMap =
      new HashMap<Class<?>, PersistAction<?>>();

  public PersistWrapper addReferenceWrapper(Class<?> parentClazz,
      ReferenceWrapper referenceWrapper) {
    referenceWrapperMap.add(parentClazz, referenceWrapper);
    return this;
  }

  /**
   * @param parentClazz 主表类
   * @param childrenProperty 从表在主表的属性值
   * @param childClazz 从表类
   * @return
   */
  public PersistWrapper addReferenceWrapper(Class<?> parentClazz, String childrenProperty,
      Class<?> childClazz) {
    referenceWrapperMap.add(parentClazz, new ReferenceWrapper(childrenProperty, childClazz));
    return this;
  }

  public Collection<ReferenceWrapper> getReferenceWrappers(Class<?> clazz) {
    return referenceWrapperMap.getValue(clazz);
  }

  public Collection<ReferenceWrapper> getPropertyWrappers(Object baseModel) {
    return getReferenceWrappers(getOrginalClass(baseModel));
  }

  private Class<?> getOrginalClass(Object entity) {
    return entity instanceof ProxyObject ? entity.getClass().getSuperclass() : entity.getClass();
  }

  public ListMap<Class<?>, ReferenceWrapper> getReferenceWrapperMap() {
    return referenceWrapperMap;
  }

  public void setReferenceWrapperMap(ListMap<Class<?>, ReferenceWrapper> property) {
    referenceWrapperMap = property;
  }

  public Map<Class<?>, PersistAction<?>> getPersistActionMap() {
    return persistActionMap;
  }

  public void setPersistActionMap(Map<Class<?>, PersistAction<?>> persistActionMap) {
    this.persistActionMap = persistActionMap;
  }

  public void addPersistAction(Class<?> clazz, PersistAction<?> persistAction) {
    persistActionMap.put(clazz, persistAction);
  }

  public PersistAction<?> getPersistAction(Object baseModel) {
    return getPersistAction(getOrginalClass(baseModel));
  }

  public PersistAction<?> getPersistAction(Class<?> clazz) {
    PersistAction<?> persistAction = persistActionMap.get(clazz);
    return persistAction != null ? persistAction : new NopPersistAction();
  }

}
