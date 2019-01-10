package org.malagu.panda.security.ui.filter;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.security.orm.Component;
import org.malagu.panda.security.orm.ComponentType;
import org.malagu.panda.security.ui.utils.ControlUtils;
import org.malagu.panda.security.ui.utils.UrlUtils;

import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;
import com.bstek.dorado.view.widget.grid.DataColumn;

@org.springframework.stereotype.Component
public class ColumnFilter extends AbstractFilter<Column> {
	
	@Override
	public void invoke(Column column) {
		if (ControlUtils.isNoSecurtiy(column)) {
			return;
		}
		if (ControlUtils.supportControlType(column)) {
			String path = UrlUtils.getRequestPath();
			String componentId = getId(column);
			if (componentId != null) {
				Component component = new Component();
				component.setComponentId(componentId);
				component.setPath(path);
				component.setComponentType(ComponentType.ReadWrite);
				if (column instanceof DataColumn) {
					if (!securityDecisionManager.decide(component)) {
						component.setComponentType(ComponentType.Read);
						if (!securityDecisionManager.decide(component)) {
							column.setIgnored(true);
							return;
						} else {
							((DataColumn) column).setReadOnly(true);;
						}
					}
				} else {
					component.setComponentType(ComponentType.Read);
					if (!securityDecisionManager.decide(component)) {
						column.setIgnored(true);
						return;
					} 
				}
			}
		}
		filterChildren(column);
	}
	
	protected String getId(Column column){
		String id = column.getName();
		if (StringUtils.isEmpty(id)) {
			id = column.getCaption();
		}
		return id;
	}
	
	protected Collection<Column> getChildren(Column column){
		if(column instanceof ColumnGroup){
			return ((ColumnGroup)column).getColumns();
		}
		return null;
	}
	
	@Override
	public boolean support(Object control) {
		return Column.class.isAssignableFrom(control.getClass());
	}




}

