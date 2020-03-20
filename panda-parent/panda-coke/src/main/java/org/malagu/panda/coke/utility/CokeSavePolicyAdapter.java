package org.malagu.panda.coke.utility;

import javax.persistence.EntityManager;
import org.apache.commons.beanutils.PropertyUtils;
import org.malagu.panda.coke.annotation.EntityParent;
import org.malagu.panda.coke.model.BaseModel;
import org.malagu.panda.coke.model.CokeBaseModel;
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

  boolean idIsNull(Object entity) {
    return entity instanceof CokeBaseModel && ((CokeBaseModel) entity).getId() == null;
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

    // 获取真实class
    clazz = BeanReflectionUtil.getClass(entity);
    EntityParent[] entityParents = clazz.getAnnotationsByType(EntityParent.class);

    // 判断是否有@EntityParent，设置父id
    if (entityParents != null && entityParents.length > 0) {
      EntityParent entityParent = entityParents[0];
      String parentName = entityParent.value();

      if (parent instanceof BaseModel<?>) {
        try {
          PropertyUtils.setProperty(entity, parentName, ((BaseModel<?>) parent).getId());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else if (entity instanceof IDetail<?> && parent instanceof BaseModel<?>) {
      Object parentId = ((IBase<?>) parent).getId();
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
