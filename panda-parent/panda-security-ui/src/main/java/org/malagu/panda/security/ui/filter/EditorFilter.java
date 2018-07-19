package org.malagu.panda.security.ui.filter;


import org.malagu.panda.security.orm.Component;
import org.malagu.panda.security.orm.ComponentType;
import org.malagu.panda.security.ui.utils.ControlUtils;
import org.malagu.panda.security.ui.utils.UrlUtils;

import com.bstek.dorado.view.widget.form.AbstractEditor;

@org.springframework.stereotype.Component
public class EditorFilter extends AbstractFilter<AbstractEditor> {

	@Override
	public void invoke(AbstractEditor editor) {
		if (ControlUtils.isNoSecurtiy(editor)) {
			return;
		}
		if (ControlUtils.supportControlType(editor)) {
			String path = UrlUtils.getRequestPath();
			String componentId = getId(editor);
			if (componentId != null) {
				Component component = new Component();
				component.setComponentId(componentId);
				component.setPath(path);
				component.setComponentType(ComponentType.ReadWrite);
				if (!securityDecisionManager.decide(component)) {
					component.setComponentType(ComponentType.Read);
					if (!securityDecisionManager.decide(component)) {
						editor.setIgnored(true);
						return;
					} else {
						editor.setReadOnly(true);;
					}
				}
			}
		}
		filterChildren(editor);
	}
	
	@Override
	public boolean support(Object control) {
		return AbstractEditor.class.isAssignableFrom(control.getClass());
	}

}

