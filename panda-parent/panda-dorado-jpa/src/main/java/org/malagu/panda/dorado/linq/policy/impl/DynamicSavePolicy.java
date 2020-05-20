package org.malagu.panda.dorado.linq.policy.impl;

import org.malagu.panda.dorado.linq.policy.SaveContext;
import org.malagu.panda.dorado.linq.policy.SavePolicy;

public interface DynamicSavePolicy extends SavePolicy {
  boolean beforeApply(SaveContext context);

}
