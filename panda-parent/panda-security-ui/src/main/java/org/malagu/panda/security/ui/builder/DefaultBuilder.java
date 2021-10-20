package org.malagu.panda.security.ui.builder;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.security.ui.utils.ControlUtils;
import org.springframework.beans.factory.annotation.Value;
import com.bstek.dorado.view.AbstractViewElement;
import com.bstek.dorado.view.manager.ViewConfig;

/**
 * @author Kevin.yang
 */
@org.springframework.stereotype.Component
public class DefaultBuilder implements Builder<AbstractViewElement> {

  @Value("${panda.componentPermissionFlat}")
  private boolean componentPermissionFlat;

  @Override
  public void build(AbstractViewElement control, ViewComponent parent, ViewComponent root, ViewConfig viewConfig) {
    if (ControlUtils.isNoSecurtiy(control)) {
      return;
    }
    if (ControlUtils.supportControlType(control)) {
      ViewComponent component = new ViewComponent();
      component.setId(control.getId());
      component.setIcon(getIcon(control));
      component.setEnabled(StringUtils.isNotEmpty(control.getId()));
      component.setName(getName(control));
      if (componentPermissionFlat) {
        root.addChildren(component);
      } else {
        parent.addChildren(component);
      }
    }
  }

  protected String getIcon(AbstractViewElement control) {
    return ">dorado/res/"
        + control.getClass().getName().replaceAll("\\.", "/").replaceAll("\\_\\$\\$\\_.*", "")
        + ".png";
  }

  protected String getName(AbstractViewElement control) {
    return control.getClass().getSimpleName().replaceAll("\\_\\$\\$\\_.*", "");
  }

  @Override
  public boolean support(Object control) {
    return false;
  }

}
