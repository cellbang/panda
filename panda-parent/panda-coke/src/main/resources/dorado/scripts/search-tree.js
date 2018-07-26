(function(win) {
	var dataSet;
	var dataTree;
	var properties = [];
	var bindConfig;
	var highlightBgColor;

	function findBindingConfig(bindingConfigName) {
		var bindingConfigs = dataTree.get("bindingConfigs")
		if (!bindingConfigs || !bindingConfigs.length) {
			return;
		}
		var bindConfig;
		if (bindingConfigs.length == 1) {
			bindConfig = bindingConfigs[0];
		} else if (!bindingConfigName) {
			for (var i = 0; i < bindingConfigs.length; i++) {
				if (bindingConfigs[i] == "bindingConfigName") {
					bindConfig = bindingConfigs[i];
					break;
				}
			}
		}
		return bindConfig;
	}
	function buildDataPath(keyword) {
		if (!keyword) {

		}
		var childrenProperty = bindConfig.childrenProperty;
		var condition = "";
		var separetor = "";
		properties.each(function(property) {
			condition += separetor + "@.get('" + property + "') && @.get('" + property + "').indexOf('" + keyword
					+ "')>-1";
			separetor = " || ";
		});
		var dataPath = childrenProperty + "(R)[" + condition + "]";
		return dataPath;
	}
	function collapse() {
		var nodes = dataTree.get("nodes");
		var node = nodes.get(0);
		if (node) {
			dataTree.set("currentNode", node);
		}
		collapseNodes(nodes);
		dataTree.refresh();
	}
	function search(keyword) {
		var dataPath = buildDataPath(keyword);
		var entities = dataSet.queryData(dataPath);
		entities.each(function(entity) {
			expandEntity(entity);
		});
	}
	function collapseNodes(nodes) {
		if (nodes && jQuery.isFunction(nodes.each)) {
			nodes.each(function(node) {
				$fly(node._dom).css("background-color", "");
				node.collapse();
				collapseNodes(node.get("nodes"));
			});
		}
	}
	function expandNode(node) {
		if (!node) {
			return;
		}
		var nodePath = [];
		while (node.get("parent.parent")) {
			var parentNode = node.get("parent");
			nodePath.push(parentNode);
			node = parentNode;
		}
		for (var i = nodePath.length - 1; i >= 0; i--) {
			nodePath[i].expand();
		}
	}
	function expandEntity(entity) {
		var oldEntity = entity;
		var entityPath = [ entity ];
		while (entity.parent.parent) { // parent -> entityList; parent.parent
			// -> parentEntity
			var parentEntity = entity.parent.parent;
			entityPath.push(parentEntity);
			entity = parentEntity;
		}
		for (var i = entityPath.length - 1; i >= 0; i--) {
			var node = dataTree.findNode(entityPath[i]);
			node.expand();
		}
		node = dataTree.findNode(oldEntity);
		node.highlight();
		$fly(node._dom).css("background-color", highlightBgColor);
	}
	function buildSearch(config) {
		dataTree = config.dataTree;
		if (!config.properties) {
			return;
		}
		properties = config.properties.split(",");

		dataSet = dataTree.get("dataSet");
		bindConfig = findBindingConfig(config.bindConfig);
		highlightBgColor = config.highlightBgColor || "#FC8800";

		return function(keyword) {
			collapse();
			if (!keyword) {
				return;
			}
			search(keyword);
		};

	}
	win.hro = win.hro || {};

	win.hro.searchTree = {
		buildSearch : buildSearch
	};

}(window));
