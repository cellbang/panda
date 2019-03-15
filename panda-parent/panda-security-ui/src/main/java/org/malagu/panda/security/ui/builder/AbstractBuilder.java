package org.malagu.panda.security.ui.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.security.ui.utils.ClassUtils;
import org.malagu.panda.security.ui.utils.ControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import com.bstek.dorado.view.AbstractViewElement;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Kevin.yang
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractBuilder<T extends AbstractViewElement> implements Builder<T> {

  @Autowired
  protected Collection<Builder> builders;
  
  @Autowired
  private ApplicationContext applicationContext;
  
  @PostConstruct
  private void init() {
    builders = new ArrayList<>(builders);
    builders.add(applicationContext.getBean(this.getClass()));
  }

  @Autowired
  private DefaultBuilder defaultBuilder;

  @Value("${panda.componentPermissionFlat}")
  private boolean componentPermissionFlat;

  @Override
  public void build(T control, ViewComponent parent, ViewComponent root) {
    if (ControlUtils.isNoSecurtiy(control)) {
      return;
    }
    ViewComponent p = null;
    if (ControlUtils.supportControlType(control)) {
      ViewComponent component = new ViewComponent();
      component.setId(getId(control));
      component.setDesc(getDesc(control));
      component.setIcon(getIcon(control));
      component.setEnabled(isEnabled(control));
      component.setName(getName(control));
      if (componentPermissionFlat) {
        root.addChildren(component);
      } else {
        parent.addChildren(component);
      }
      p = component;
    } else {
      p = parent;
    }

    buildChildren(control, p, root);
  }

  protected void buildChildren(T control, ViewComponent parent, ViewComponent root) {
    Collection children = getChildren(control);
    if (children != null) {
      for (Object child : children) {
        if (child instanceof AbstractViewElement) {
          boolean exist = false;
          AbstractViewElement c = (AbstractViewElement) child;
          for (Builder builder : builders) {
            if (builder.support(c)) {
              exist = true;
              builder.build(c, parent, root);
              break;
            }
          }
          if (!exist) {
            defaultBuilder.build(c, parent, root);
          }
        }
      }
    }
  }

  protected String getId(T control) {
    if (control instanceof Component) {
      return ((Component) control).getId();
    }
    return null;
  }

  protected String getIcon(T control) {
    return ">dorado/res/" + control.getClass().getName().replaceAll("\\.", "/").replaceAll("\\_\\$\\$\\_.*", "") + ".png";
  }

  protected String getName(T control) {
    return control.getClass().getSimpleName().replaceAll("\\_\\$\\$\\_.*", "");
  }

  protected boolean isEnabled(T control) {
    return StringUtils.isNotEmpty(getId(control));
  }

  protected Collection getChildren(T control) {
    return Collections.EMPTY_LIST;
  }

  protected String getDesc(T control) {
    return ControlUtils.getSecurityDesc(control);
  }

  @Override
  public boolean support(Object control) {
    Class<?> clazz = ClassUtils.getGenericType(this.getClass(), 0);
    return clazz.isAssignableFrom(control.getClass());
  }

}
