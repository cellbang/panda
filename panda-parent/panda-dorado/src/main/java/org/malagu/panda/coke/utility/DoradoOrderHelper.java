package org.malagu.panda.coke.utility;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Order;

public class DoradoOrderHelper {
  public static Criteria desc(Criteria criteria, String property) {
    return addOrder(criteria, property, true);
  }

  public static Criteria asc(Criteria criteria, String property) {
    return addOrder(criteria, property, false);
  }

  public static Criteria addOrder(Criteria criteria, String property, boolean desc) {
    if (criteria == null) {
      criteria = new Criteria();
    }
    criteria.addOrder(new Order(property, desc));
    return criteria;
  }
}
