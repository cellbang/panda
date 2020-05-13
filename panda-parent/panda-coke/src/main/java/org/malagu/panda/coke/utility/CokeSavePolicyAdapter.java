package org.malagu.panda.coke.utility;

import javax.persistence.EntityManager;
import org.malagu.panda.coke.annotation.EntityDef;
import org.malagu.panda.coke.annotation.EntityParent;
import org.malagu.panda.coke.model.BaseModel;
import org.malagu.panda.coke.model.IBase;
import org.malagu.panda.coke.model.IDetail;
import org.malagu.panda.dorado.linq.policy.SaveContext;
import org.malagu.panda.dorado.linq.policy.SavePolicy;
import org.malagu.panda.security.ContextUtils;
import org.malagu.panda.security.orm.User;
import org.xobo.toolkit.BeanReflectionUtil;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;

public class CokeSavePolicyAdapter implements SavePolicy {
  Object getId(Object entity) {
    Object id = doGetId(entity);
    if (UNKNOWN.equals(id) || id == null) {
      return null;
    }
    return id;
  }

  private static Integer UNKNOWN = -1;

  Object doGetId(Object entity) {
    if (entity instanceof IBase) {
      return ((IBase<?>) entity).getId();
    }
    // 获取真实class
    Class<?> clazz = BeanReflectionUtil.getClass(entity);
    EntityDef[] entityDefs = clazz.getAnnotationsByType(EntityDef.class);
    // 判断是否有@EntityDef
    if (entityDefs != null && entityDefs.length > 0) {
      EntityDef entityParent = entityDefs[0];
      String idName = entityParent.id();
      return BeanReflectionUtil.getProperty(entity, idName);
    }

    return UNKNOWN;
  }

  boolean idIsNull(Object entity) {
    return doGetId(entity) == null;
  }

  @Override
  public void apply(SaveContext context) {
    if (!beforeApply(context)) {
      return;
    }

    Object entity = context.getEntity();

    EntityManager entityManager = context.getEntityManager();
    EntityState state = EntityUtils.getState(entity);

    User user = ContextUtils.getLoginUser();

    if (EntityState.NEW.equals(state) || idIsNull(entity)) {
      if (beforeInsert(context)) {
        BaseEntityUtil.setInsertMeta(user, entity);
        try {
          entityManager.persist(entity);
        } catch (Exception e) {
          e.printStackTrace();
        }
        afterInsert(context);
      }
    } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
      if (beforeUpdate(context)) {
        BaseEntityUtil.setUpdateMeta(user, entity);
        try {
          entityManager.merge(entity);
        } catch (Exception e) {
          e.printStackTrace();
        }
        afterUpdate(context);
      }
    } else if (EntityState.DELETED.equals(state)) {
      if (beforeDelete(context)) {
        BaseEntityUtil.setDelMeta(user, entity);
        try {
          entityManager.merge(entity);
        } catch (Exception e) {
          e.printStackTrace();
        }
        afterDelete(context);
      }

    }
  }

  @SuppressWarnings("unchecked")
  public void setParentId(SaveContext context) {
    Object entity = context.getEntity();
    Object parent = context.getParent();
    Class<?> clazz = entity.getClass();
    if (parent == null) {
      return;
    }

    // 获取真实class
    clazz = BeanReflectionUtil.getClass(entity);
    EntityParent[] entityParents = clazz.getAnnotationsByType(EntityParent.class);
    EntityDef[] entityDefs = clazz.getAnnotationsByType(EntityDef.class);

    Object parentId = getId(parent);
    if (parentId == null) {
      return;
    }

    // 判断是否有@EntityParent，设置父id
    if (entityParents != null && entityParents.length > 0) {
      EntityParent entityParent = entityParents[0];
      String parentName = entityParent.value();

      if (parent instanceof BaseModel<?>) {
        try {
          BeanReflectionUtil.setProperty(entity, parentName, parentId);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else if (entityDefs != null && entityDefs.length > 0) {
      EntityDef entityParent = entityDefs[0];
      String pidName = entityParent.parentId();
      BeanReflectionUtil.setProperty(entity, pidName, parentId);
    } else if (entity instanceof IDetail<?> && parent instanceof BaseModel<?>) {
      ((IDetail<Object>) entity).setParentId(parentId);
    }
  }

  public boolean beforeApply(SaveContext context) {
    return true;
  }

  public boolean beforeDelete(SaveContext context) {
    return true;
  }

  public void afterDelete(SaveContext context) {

  }

  public boolean beforeInsert(SaveContext context) {
    setParentId(context);
    return true;
  }

  public void afterInsert(SaveContext context) {

  }

  public boolean beforeUpdate(SaveContext context) {
    return true;
  }

  public void afterUpdate(SaveContext context) {

  }

}
