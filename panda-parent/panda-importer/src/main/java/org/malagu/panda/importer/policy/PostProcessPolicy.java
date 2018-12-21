package org.malagu.panda.importer.policy;

import org.springframework.beans.factory.BeanNameAware;

public interface PostProcessPolicy extends BeanNameAware {
  String getCaption();

  void apply(Context context) throws Exception;

}
