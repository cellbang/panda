package org.malagu.panda.security.ui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.Panel;

@org.springframework.stereotype.Component("maintain.panelBuilder")
public class PanelBuilder extends AbstractBuilder<Panel> {

	protected Collection<Component> getChildren(Panel panel){
		Collection<Component> children =new ArrayList<Component>();
		children.addAll((Collection<? extends Component>) panel.getTools());
		children.addAll((Collection<? extends Component>) panel.getChildren());
		children.addAll((Collection<? extends Component>) panel.getButtons());
		return children;
	}

	
	protected String getId(Panel panel){
		String id=panel.getId();
		if(StringUtils.isEmpty(id)){
			id=panel.getCaption();
		}
		return id;
	}
	
	protected String getDesc(Panel panel){
		String desc = super.getDesc(panel);
		if (desc != null) {
			return desc;
		}
		if(StringUtils.isNotEmpty(panel.getId())){
			return panel.getCaption();		
		}
		return null;
	}


}
