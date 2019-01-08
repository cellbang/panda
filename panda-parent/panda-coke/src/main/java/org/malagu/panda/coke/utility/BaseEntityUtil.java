package org.malagu.panda.coke.utility;

import java.util.Date;
import org.malagu.panda.coke.model.BaseModel;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.security.ContextUtils;
import org.malagu.panda.security.orm.User;

public class BaseEntityUtil {

  public static void setMeta(Object model) {
    if (model instanceof BaseModel<?>) {
      if (((BaseModel<?>) model).getId() == null) {
        setInsertMeta(model);
      } else {
        setUpdateMeta(model);
      }
    }
  }

  public static void setInsertMeta(Object model) {
    User user = ContextUtils.getLoginUser();
    setInsertMeta(user, model);
  }

  public static void setUpdateMeta(Object model) {
    User user = ContextUtils.getLoginUser();
    setUpdateMeta(user, model);
  }

  public static void setInsertMeta(User user, Object model) {
    if (model instanceof BaseModel) {
      ((BaseModel<?>) model).setCreateDate(new Date());
      // ((BaseModel) model).setCreateUser(user.getUsername());
    }
    setUpdateMeta(user, model);
  }

  public static void setUpdateMeta(User user, Object model) {
    if (model instanceof BaseModel) {
      ((BaseModel<?>) model).setUpdateDate(new Date());
      // ((BaseModel) model).setUpdateUser(user.getUsername());
    }
  }

  public static void setDelMeta(Object model) {
    User user = ContextUtils.getLoginUser();
    setDelMeta(user, model);
  }

  public static void setDelMeta(User user, Object model) {
    if (model instanceof BaseModel) {
      ((BaseModel<?>) model).setDeleted(true);
      setUpdateMeta(user, model);
    }
  }

  public static void persist(Object entity) {
    JpaUtil.persist(entity);
  }
}
