package org.malagu.panda.importer.policy.impl;

import java.util.List;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.policy.PostProcessPolicy;

public class DefaultPostProcessPolicy implements PostProcessPolicy {
  final public static String BEAN_ID = "importer.defaultPostProcessPolicy";
  private String beanName;

  @Override
  public void apply(Context context) throws Exception {
    List<Object> resultList = context.getResultList();
    int i = 0;
    for (Object entity : resultList) {
      JpaUtil.persist(entity);
      if (i++ % 100 == 0) {
        JpaUtil.persistAndFlush(entity);
        JpaUtil.getEntityManager(entity).clear();
      } else {
        JpaUtil.persist(entity);
      }
    }
  }


  @Override
  public String getCaption() {
    return "保存";
  }


  @Override
  public void setBeanName(String name) {
    beanName = name;
  }


  public String getBeanName() {
    return beanName;
  }

}
