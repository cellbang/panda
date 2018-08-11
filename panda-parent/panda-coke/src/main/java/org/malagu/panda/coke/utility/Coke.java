package org.malagu.panda.coke.utility;

import java.util.Map;
import org.malagu.panda.coke.querysupporter.service.DoradoCriteriaBuilder;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.lin.Linq;
import com.bstek.dorado.data.provider.Criteria;

public class Coke {
  private static DoradoCriteriaBuilder doradoCriteriaBuilder;

  public static DoradoCriteriaBuilder getDoradoCriteriaBuilder() {
    if (doradoCriteriaBuilder != null) {
      return doradoCriteriaBuilder;
    }

    doradoCriteriaBuilder =
        (DoradoCriteriaBuilder) org.malagu.linq.JpaUtil.getApplicationContext()
            .getBean(DoradoCriteriaBuilder.BEAN_ID);
    return doradoCriteriaBuilder;
  }

  public static <T> Linq query(Class<T> domainClass, Criteria criteria,
      Map<String, Object> parameterMap) {
    criteria =
        getDoradoCriteriaBuilder().mergeQueryParameterCriteria(domainClass, parameterMap, criteria);
    return JpaUtil.linq(domainClass).where(criteria);
  }

  public static <T> Linq query(Class<T> domainClass, Class<?> resultClass, Criteria criteria,
      Map<String, Object> parameterMap) {
    criteria =
        getDoradoCriteriaBuilder().mergeQueryParameterCriteria(domainClass, parameterMap, criteria);
    return JpaUtil.linq(domainClass, resultClass).where(criteria);
  }

}
