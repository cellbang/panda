package org.malagu.panda.security.ui.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.security.ui.utils.DoradoUtil;
import org.malagu.panda.security.ui.utils.DoradoUtil.AutoFormDataControlWrapper;
import org.springframework.stereotype.Component;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.FormElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;

@Component("maintain.formElementBuilder")
public class FormElementBuilder extends AbstractBuilder<FormElement> {

  protected Collection<Control> getChildren(FormElement element) {
    if (element.getEditor() != null) {
      return Arrays.asList(element.getEditor());
    }
    return null;
  }

  private boolean useParentId;

  protected String getId(FormElement element) {
    String id = element.getId();
    if (StringUtils.isNotEmpty(id)) {
      return id;
    }

    ViewElement parentViewElement = element.getParent();

    String prefix = "";
    if (useParentId) {
      String pid = parentViewElement.getId();
      if (StringUtils.isNotEmpty(pid)) {
        prefix = "#" + pid + ".";
      }
    }

    String controlId = null;

    if (element instanceof AutoFormElement) {
      controlId = ((AutoFormElement) element).getName();
    }

    if (StringUtils.isEmpty(controlId)) {
      controlId = element.getProperty();
    }

    if (StringUtils.isEmpty(controlId)) {
      controlId = element.getLabel();
    }

    // 使用
    if (StringUtils.isEmpty(controlId)) {
      if (parentViewElement instanceof AutoForm) {
        EntityDataType entityDataType = ((AutoForm) parentViewElement).getDataType();
        Map<String, PropertyDef> dataTypePropertyDefs = null;
        if (entityDataType != null) {
          dataTypePropertyDefs = entityDataType.getPropertyDefs();
        }
        controlId = getFormElementLabel(element, dataTypePropertyDefs);
      }
    }
    return prefix + controlId;
  }


  private String getFormElementLabel(FormElement element, Map<String, PropertyDef> dataTypePropertyDefs) {
    String property = element.getProperty();
    if (StringUtils.isNotEmpty(property) && dataTypePropertyDefs != null) {
      PropertyDef pd = dataTypePropertyDefs.get(property);
      if (pd != null && StringUtils.isNotEmpty(pd.getLabel())) {
        return pd.getLabel();
      }
    }
    return property;
  }


  @Override
  protected String getDesc(FormElement formElement, ViewConfig viewConfig) {
    String desc = super.getDesc(formElement, viewConfig);

    View view = viewConfig.getView();
    ViewElement parentViewElement = formElement.getParent();
    if (parentViewElement instanceof AutoForm) {
      AutoForm autoForm = (AutoForm) parentViewElement;
      EntityDataType entityDataType = autoForm.getDataType();
      if (entityDataType == null) {
        entityDataType = DoradoUtil.getEntityDataType(view, new AutoFormDataControlWrapper(autoForm));
      }

      if (entityDataType != null) {
        desc = DoradoUtil.getLabel(entityDataType, formElement.getProperty());
      }
    }

    if (desc != null) {
      return desc;
    }
    if (StringUtils.isNotEmpty(formElement.getId())) {
      return formElement.getId();
    }
    if (formElement.getLabel() != null) {
      return formElement.getLabel();
    }
    return desc;
  }


}
