$namespace("coke");
coke.renderDataGridError = function(arg, errorColor) {
	var entity = arg.data;
	var column = arg.column;
	var property = column.get("property");
	errorColor = errorColor || "#FFE1E2";
	var messages = entity.getMessages(property);
	if (messages) {
		var error = false;
		messages.each(function(msg) {
			if (msg.state == "error") {
				error = true;
			}
		});
	}

	qdom = $fly(arg.dom);
	entity.originalBackgroundColor = entity.originalBackgroundColor || qdom.css("background-color");
	var finalColor = arg.finalColor || entity.originalBackgroundColor;
	if (error) {
		arg.finalColor = errorColor;
		qdom.css("background-color", errorColor);
	} else {
		qdom.css("background-color", finalColor);
	}
	arg.processDefault = true;
}

coke.smartExecute = function(action, parameter, executeCallback, cacheCallback) {
	var strParameter = dorado.JSON.stringify(parameter);
	if (action.strParameter != strParameter) {
		action.strParameter = strParameter;
		action.set("parameter", parameter);
		if (jQuery.isFunction(executeCallback)) {
			executeCallback();
		} else {
			if (action instanceof dorado.widget.DataSet) {
				action.flushAsync();
			} else if (action instanceof dorad.widget.Action) {
				action.execute();
			}
		}
	} else {
		if (jQuery.isFunction(cacheCallback)) {
			cacheCallback();
		}
	}
}
coke.insertItem = function(dataSet, dataPath, dialog, data) {
	data = data || {};
	var list = dataSet.getData(dataPath);
	if (list === undefined) {
		list = dataSet.getData();
		if (list === undefined) {
			dataSet.setData(dataPath, {});
			list = dataSet.getData(dataPath);
		}
	}
	if (list) {
		data = list.insert(data);
		if (dialog) {
			dialog.show();
		}
		return data;
	} else {
		dorado.MessageBox.alert('不能添加数据。');
	}
}
coke.insertChildItem = function(dataTree, childrenName, dialog, insertDataFunc) {
	childrenName = childrenName || "children";

	var currentEntity = dataTree.get("currentEntity");
	if (currentEntity) {
		dataTree.get("currentNode").expand();
		// newEntity = currentEntity.createChild(childrenName, data);
		newEntity = currentEntity.get(childrenName).insert();
		dataTree.set("currentEntity", newEntity);
		setTimeout(function() {
			if (jQuery.isFunction(insertDataFunc)) {
				newEntity.set(insertDataFunc(newEntity));
			}
			dataTree.set("currentEntity", newEntity);
		}, 200);
		if (dialog) {
			dialog.show();
		}
	}
}
coke.insertBrotherItem = function(dataTree, childrenName, dialog, insertDataFunc) {
	childrenName = childrenName || "children";

	var currentEntity = dataTree.get("currentEntity");
	if (currentEntity) {
		newEntity = currentEntity.createBrother();
		dataTree.set("currentEntity", newEntity);
		if (jQuery.isFunction(insertDataFunc)) {
			newEntity.set(insertDataFunc(newEntity));
		}

		if (dialog) {
			dialog.show();
		}
	} else {
		dataTree.get("dataSet").getData(dataTree.get("dataPath")).insert();
	}
}

coke.isItemEditable = function(buttonEdit) {
	return !buttonEdit || !buttonEdit.get("disabled");
}

coke.editItem = function(dataSet, dataPath, dialog, buttonEdit) {
	if (coke.isItemEditable(buttonEdit)) {
		var entity = dataSet.getData(dataPath);
		if (entity) {
			dialog && dialog.show();
		} else {
			dorado.widget.NotifyTipManager.notify("没有可编辑的记录!");
		}
	}
}

coke.viewItem = function(dataSet, dataPath, dialog) {
	dataSet.set("readOnly", true);
	var entity = dataSet.getData(dataPath);
	if (entity) {
		dialog.show();
	} else {
		dorado.widget.NotifyTipManager.notify("没有可查看的记录!");
	}
}



/*
 * coke.deleteItem(dataSet, dataPath, updateAction, callBack);
 */
coke.deleteItem = function(dataSet, dataPath, updateAction, callBack) {
	var entity = dataSet.getData(dataPath);
	if (entity) {
		dorado.MessageBox.confirm("确认要删除选中的记录么？", {
			icon: "WARNING",
			title: "删除记录",
			callback: function() {
				entity.remove();
				updateAction.execute(callBack);
			}
		});
	} else {
		dorado.widget.NotifyTipManager.notify('没有可删除的记录。');
	}
}
/*
 * coke.deleteItems(dataGrid, updateAction, config);
 */
coke.deleteItems = function(dataGrid, updateAction, config) {
	if (!config) {
		config = {};
	}


	var selection = coke.getSelections(dataGrid);

	var names = [];
	var error = "";
	if (config.display) {
		selection.each(function(item) {
			if (jQuery.isFunction(config.beforeDel)) {
				var result = config.beforeDel(item);
				if (result) {
					error += result;
				}
			}
			names.push(item.get(config.display))
		});
	}

	if (error) {
		if (jQuery.isFunction(config.error)) {
			config.error(names, error);
		} else {
			dorado.MessageBox.alert(error);
		}
		return;
	}

	var content = "确认要删除选中的记录。";
	if (names.length) {
		content = "确认要删除选中的记录: \n\t" + names.join(",") + " 。";
	}
	dorado.MessageBox.confirm(content, {
		icon: "WARNING",
		title: "删除记录",
		callback: function() {
			selection.each(function(item) {
				item.remove();
			});
			if (jQuery.isFunction(config.updateAction)) {
				config.updateAction();
			} else if (updateAction) {
				updateAction.execute(function() {
					if (jQuery.isFunction(config.afterDel)) {
						config.afterDel()
					}
				});
			} else {
				console.log("未定义删除动作。");
			}
		}
	});
}

coke.saveItem = function(dataSet, dataPath, updateAction, dialog, callback) {
	var entity = dataSet.getData(dataPath);
	if (updateAction.get("hasUpdateData")) {
		updateAction.execute(function() {
			dialog && dialog.hide();
			if (callback instanceof Function) {
				callback();
			}
		});
	} else {
		dialog && dialog.hide();
		if (callback instanceof Function) {
			callback();
		}
	}
}

coke.cancelItem = function(dataSet, dataPath, dialog, callback) {
	var entity = dataSet.getData(dataPath);
	if (entity && entity.isDirty()) {
		dorado.MessageBox.confirm("确认放弃当前修改？", {
			title: "关闭编辑窗口",
			callback: function() {
				entity.cancel();
				dialog && dialog.hide();
				if (typeof callback === "function") {
					callback();
				}
			}
		});
	} else {
		dialog && dialog.hide();
		if (typeof callback === "function") {
			callback();
		}
	}
}

coke.queryItem = function(dataSet, dataSetQuery, dataPath) {
	var data = dataSetQuery.getData();
	if (data && data.toJSON) {
		data = data.toJSON();
	}
	dataSet.set("parameter", data).flushAsync();
}

coke.queryReferenceItem = function(entity, reference, autoformQuery) {
	var queryJson = autoformQuery.get("entity").toJSON();
	var parameter = reference.get("parameter");
	var lastQueryJson = autoformQuery.lastQueryJson;
	if (lastQueryJson) {
		for (var p in lastQueryJson) {
			parameter.remove(p);
		}
	}
	parameter.put(queryJson);
	entity.reset(reference.get("name"));
	autoformQuery.lastQueryJson = queryJson;
}

coke.resetQuery = function(dataSetQuery) {
	dataSetQuery.setData({});
}

coke.getEntity = function(dataGrid) {
	if (!type) {
		type = "current";
	}

	var selection = dataGrid.get("selection");
	var current;
	if (selection) {
		if (selection.length == 1) {
			current = selection[0];
		} else {
			dorado.MessageBox.alert("请选择一条记录.");
		}
	} else {
		current = dataGrid.get("dataSet").getData().current;
	}
	return current;
}

coke.getSelections = function(dataControl, type) {
	if (!type) {
		type = "current";
	}
	if (!dataControl) {
		console.error("未找到对应的 DataControl,无法处理。");
		return;
	}

	var selection = dataControl.get("selection");
	var list;
	if (selection && selection.length > 0) {
		list = selection;
	} else if (type == "all") {
		list = dataControl.get("dataSet").getData(dataControl.get("dataPath"));
	} else if (type == "current") {
		var current;
		if (dorado.widget.DataTree && dataControl instanceof dorado.widget.DataTree) {
			current = dataControl.get("dataSet").getData("!" + dataControl.get("currentNodeDataPath"));
		} else {
			current = dataControl.get("dataSet").getData(dataControl.get("dataPath")).current;
		}
		list = [];
		list.push(current);
	} else {
		list = [];
	}
	return list;
}

coke.dataRowClick = function(rowList, clickCallback, doubleClickCallback) {
	var timer;

	var singleClick = function() {
		clearTimeout(timer);
		timer = setTimeout(function() {
			if (clickCallback instanceof Function) {
				clickCallback();
			}
		}, 200);
	}

	var doubleClick = function() {
		clearTimeout(timer);
		if (doubleClickCallback instanceof Function) {
			doubleClickCallback();
		}
	}

	rowList.addListener("onDataRowClick", singleClick);
	rowList.addListener("onDataRowDoubleClick", doubleClick);
}

coke.generateMyBatisCondition = function(dataType, alias, parameterPrefix) {
	var names = [];
	var result = "";

	alias = alias || "t.";
	parameterPrefix = parameterPrefix || "";
	if (alias && !alias.endsWith(".")) {
		alias += ".";
	}
	if (parameterPrefix && !parameterPrefix.endsWith(".")) {
		parameterPrefix += ".";
	}
	viewMain.get("@" + dataType).get("propertyDefs").each(function(item) { names.push(item.get("name")) });

	function toUnderscore(str) {
		return str.replace(/([A-Z])/g, function($1) { return "_" + $1.toLowerCase(); });
	}

	names.forEach(function(name) {
		var columnName = toUnderscore(name);
		result += `<if test="${parameterPrefix}${name} != null and ${parameterPrefix}${name} != ''">\n  AND ${alias}${columnName} = #{${parameterPrefix}${name}}\n</if>\n`;
	});
	console.log(result);
}

function setDefaultValue(control, attribute, defaultValue) {
	if (!control.get(attribute)){
		control.set(attribute, defaultValue);
	}
}

coke.autoAction = function(view, config) {
	if (typeof config === "string") {
		config = {
			name: config
		}
	}

	var dateSetId;
	var updateActionId;
	var currentPath, defaultCurrentPath;
	var listPath, defaultListPath;
	var dataSetQueryId = "dataSet" + config.name + "Query";
	var autoformQueryId = "autoform" + config.name + "Query";
	var tree = config.tree;
	var caption = config.caption;

	if (config.parentName) {
		dateSetId = "dataSet" + config.parentName;
		updateActionId = "updateAction" + config.parentName;
		var childname = config.name.substr(0, 1).toLowerCase() + config.name.substr(1) + "s";
		defaultCurrentPath = "#.#" + childname;
		defaultListPath = "#." + childname;
	} else {
		dateSetId = "dataSet" + config.name;
		updateActionId = "updateAction" + config.name;
		defaultCurrentPath = "#";
		defaultListPath = "";
	}
	
	if (config.currentPath && !config.listPath){
		config.listPath = config.currentPath.replace(/#([^#]*)$/, '$1');
	}
	
	currentPath = config.currentPath || defaultCurrentPath;
	listPath = config.listPath || defaultListPath;

	var dataSet = config.dataSet || view.id(dateSetId);
	var dataSetQuery = config.dataSetQuery || view.id(dataSetQueryId);
	if (dataSetQuery) {
		if (!dataSetQuery.getData()) {
			dataSetQuery.setData({});
		}
	}

	var updateAction = config.updateAction || view.id(updateActionId);

	var dialog = config.dialog || view.id("dialog" + config.name);
	var dataGrid = config.dataGrid || view.id("dataGrid" + config.name);
	var dataTree = config.dataTree || view.id("dataTree" + config.name)
	var dataTreeGrid = config.dataTreeGrid || view.id("dataTreeGrid" + config.name)
	var children = config.children || "children";

	var buttonEdit = config.buttonEdit || view.id("buttonEdit" + config.name);
	var autoformQuery = config.autoform || view.id("autoForm" + config.name + "Query");

	currentPath = config.currentPath || currentPath;
	listPath = config.listPath || listPath;

	view["insert" + config.name] = view["insert" + config.name] || function(args) {
		if (tree){
			view["insertBrother" + config.name]();
			return;
		}
		var insertedEntity;
		if (args && typeof args.insertData == "function") {
			insertedEntity = coke.insertItem(dataSet, listPath, dialog, args.insertData());
		} else if (typeof config.insertData == "object") {
			insertedEntity = coke.insertItem(dataSet, listPath, dialog, jQuery.extend(true, {}, config.insertData));
		} else if (typeof config.insertData == "function") {
			insertedEntity = coke.insertItem(dataSet, listPath, dialog, config.insertData());
		} else if (args && typeof args.insertData == "function") {
			insertedEntity = coke.insertItem(dataSet, listPath, dialog, args.insertData());
		} else {
			insertedEntity = coke.insertItem(dataSet, listPath, dialog);
		}
		if (insertedEntity) {
			var onInsert = (args && args.onInsert) || config.onInsert;
			if (jQuery.isFunction(onInsert)) {
				onInsert(insertedEntity);
			}
		}
	};

	view["insertChild" + config.name] = view["insertChild" + config.name] || function(args) {
		var insertedEntity;
		var insertDataFunc;
		if (args && typeof args.insertData == "function") {
			insertDataFunc = args.insertData;
		} else if (typeof config.insertData == "object") {
			insertDataFunc = function() {
				return jQuery.extend(true, {}, config.insertData);
			}
		} else if (typeof config.insertData == "function") {
			insertDataFunc = config.insertData;
		}
		insertedEntity = coke.insertChildItem(dataTree, children, dialog, insertDataFunc);

		if (insertedEntity) {
			var onInsert = (args && args.onInsert) || config.onInsert;
			if (jQuery.isFunction(onInsert)) {
				onInsert(insertedEntity);
			}
		}
	};

	view["insertBrother" + config.name] = view["insertBrother" + config.name] || function(args) {
		var insertedEntity;
		var insertDataFunc;
		if (args && typeof args.insertData == "function") {
			insertDataFunc = args.insertData;
		} else if (typeof config.insertData == "object") {
			insertDataFunc = function() {
				return jQuery.extend(true, {}, config.insertData);
			}
		} else if (typeof config.insertData == "function") {
			insertDataFunc = config.insertData;
		}
		insertedEntity = coke.insertBrotherItem(dataTree, children, dialog, insertDataFunc);

		if (insertedEntity) {
			var onInsert = (args && args.onInsert) || config.onInsert;
			if (jQuery.isFunction(onInsert)) {
				onInsert(insertedEntity);
			}
		}
	};
	
	view["getCurrent" + config.name] = view["getCurrent" + config.name] || function() {
		return dataSet.getData(currentPath);;
	}


	view["edit" + config.name] = view["edit" + config.name] || function() {
		var entity = dataSet.getData(currentPath);
		if (entity && jQuery.isFunction(config.beforeEdit)) {
			config.beforeEdit(entity);
		}
		if (entity && jQuery.isFunction(config.onEdit)) {
			config.onEdit(entity);
		}
		coke.editItem(dataSet, currentPath, dialog);
	};

	view["view" + config.name] = view["view" + config.name] || function() {
		view.get("^editWidget" + config.name).set("visible", false);
		view.get("^readOnlyWhenView" + config.name).set("readOnly", true);
		var entity = dataSet.getData(currentPath);
		addActionInterceptor("before", "View", entity);
		coke.viewItem(dataSet, currentPath, dialog);
	};

	view["del" + config.name] = view["del" + config.name] || function() {
		coke.deleteItems(dataGrid || dataTree || dataTreeGrid, updateAction, config);
	};

	function addActionInterceptor(type, action, args) {
		var realAction = config[type + action];
		if (jQuery.isFunction(realAction)) {
			realAction(args);
		}
	}

	view["save" + config.name] = view["save" + config.name] || function() {
		var entity = dataSet.getData(currentPath);
		addActionInterceptor("before", "Save", entity);
		coke.saveItem(dataSet, currentPath, updateAction, dialog, config.afterSave);
		addActionInterceptor("after", "Save", entity);

	};

	view["cancel" + config.name] = view["cancel" + config.name] || function() {
		var entity = dataSet.getData(currentPath);
		addActionInterceptor("before", "Cancel", entity);
		coke.cancelItem(dataSet, currentPath, dialog);
		addActionInterceptor("after", "Cancel", entity);
	};

	view["query" + config.name] = view["query" + config.name] || function() {
		coke.queryItem(dataSet, dataSetQuery, listPath);
	};

	view["queryReset" + config.name] = view["queryReset" + config.name] || function() {
		coke.resetQuery(dataSetQuery);
	};

	var actions = {
		"Insert": {
			"iconClass" : "fa fa-plus ck-outline-primary",
			"exClassName": "ck-outline-primary",
			"caption": "添加"
		},
		"InsertChild": {
			"iconClass": "fa fa-plus ck-outline-primary",
			"exClassName": "ck-outline-primary",
			"caption": "添加(子)"
		},
		"InsertBrother": {
			"iconClass": "fa fa-plus ck-icon-primary",
			"exClassName": "ck-btn-primary",
			"caption": "添加(平)"
		},
		"Edit": {
			"iconClass": "fa fa-pencil",
			"caption": "编辑"
		},
		"View": {
			"iconClass": "fa fa-eye",
			"caption": "查看"
		},
		"Del": {
			"iconClass": "fa fa-minus",
			"caption": "删除"
		},
		"Save": {
			"iconClass": "fa fa-check ck-icon-primary",
			"exClassName": "ck-btn-primary",
			"caption": "保存"
		},
		"Cancel": {
			"iconClass": "fa fa-times",
			"caption": "取消"
		},
		"Query": {
			"iconClass": "fa fa-search",
			"caption": "查询"
		},
		"QueryReset": {
			"iconClass": "fa fa-undo",
			"caption": "重置"
		}

	};
	for (var action in actions) {
		var component = view.id("button" + action + config.name);
		var actionFunction = view[action.substr(0, 1).toLowerCase() + action.substr(1) + config.name];
		if (!component) {
			continue;
		}

		var configs = actions[action];
		for (var p in configs) {
			var value = component.get(p);
			if (!value) {
				component.set(p, configs[p]);
			}
		}
		if (actionFunction) {
			component.bind("onClick", actionFunction);
		}
	}

	if (dialog) {
		var iconClass = dialog.get("iconClass");
		if (!iconClass) {
			dialog.set("iconClass", iconClass);
		}
		
		setDefaultValue(dialog, "iconClass", "fa fa-pencil-square-o");
		setDefaultValue(dialog, "caption", "编辑");

		dialog.bind("beforeClose", function(self, arg) {
			view["cancel" + config.name]();
			arg.processDefault = false;
		});
		dialog.bind("beforeShow", function(self, arg) {
		});
		dialog.bind("beforeHide", function(self, arg) {
			view.get("^editWidget" + config.name).set("visible", true);
			dataSet.set("readOnly", false);
			view.get("^readOnlyWhenView" + config.name).set("readOnly", false);

			if (jQuery.isFunction(dialog.callback)) {
				dialog.callback();
				dialog.callback = null;
			}

			if (jQuery.isFunction(config.beforeDialogHide)) {
				config.beforeDialogHide(self, arg);
			}
		});
	}

	if (dataGrid && dialog) {
		if (config.onDataRowDoubleClick === undefined) {
			dataGrid.bind("onDataRowDoubleClick", function(self, arg) {
				view["edit" + config.name](self, arg);
			});
		} else if (jQuery.isFunction(config.onDataRowDoubleClick)) {
			dataGrid.bind("onDataRowDoubleClick", config.onDataRowDoubleClick);
		}
	}

	if (autoformQuery) {
		autoformQuery.bind("onKeyPress", function(self, arg) {
			if (arg.keyCode == 13) {
				var queryFunc = view["query" + config.name];
				if (jQuery.isFunction(queryFunc)) {
					queryFunc();
				}
			}
		});
	}
};

function travelObject(parent, property, callback) {
	var value;
	if (property || property === 0) {
		value = parent[property];
	} else {
		value = parent;
	}
	if (jQuery.isPlainObject(value)) {
		for (var p in value) {
			travelObject(value, p, callback);
		}
	} else if (jQuery.isArray(value)) {
		for (var i = 0; i < value.length; i++) {
			travelObject(value, i, callback);
		}
	}

	if (property) {
		if (callback && callback instanceof Function) {
			callback(value, property);
		}
	}
}

$namespace("coke.dropDown")
coke.dropDown.enableDynaFilter = function(dropDown, config) {
	var filterCallback = config.filterCallback,
		filterProperty = config.filterProperty || "filterValue",
		dataSet = config.dataSet,
		dataPath = config.dataPath || "#";
	minFilterInterval = config.minFilterInterval || 300;

	dropDown.bind("onOpen", function(self, arg) {
		var editor = self._editor;
		if (editor instanceof dorado.widget.AbstractTextBox) {
			var dropDown = self;
			var filterFn = self._onTextEditedListener = function() {
				if (dropDown.get("opened")) {
					dorado.Toolkits.setDelayedAction(dropDown, "$filterTimeId", function() {
						var text = editor.doGetText();
						if (jQuery.isFunction(filterCallback)) {
							filterCallback(text);
						} else if (dataSet) {
							var p = {};
							p[filterProperty] = text;
							coke.mergeParameter(dataSet, p);
							dataSet.flushAsync();
						} else {
							console.log("typing: " + text);
						}
					}, minFilterInterval);
				}
			};
			editor.addListener("onTextEdit", filterFn);
		}
	});

	dropDown.bind("onClose", function(self, arg) {
		var editor = self._editor;
		if (editor instanceof dorado.widget.AbstractTextBox) {
			editor.removeListener("onTextEdit", self._onTextEditedListener);
		}
		if (config.autoAssignValueOnClose) {
			coke.dropDown.autoSelectOnClose(dropDown, arg, dataSet, dataPath);
		}
	});
}
coke.dropDown.autoSelectOnClose = function(dropDown, arg, dataSet, dataPath) {
	var editor = arg.editor;
	var dataPath = dataPath || "#";

	if (!arg.selectedValue && dataSet) {
		var data = dataSet.getDataAsync(dataPath, function(data) {
			if (data && editor && arg.editor.get("value")) {
				arg.selectedValue = data.toJSON();

				if (arg.selectedValue !== undefined) {
					dropDown.fireEvent("onValueSelect", dropDown, arg);
					if (arg.processDefault && arg.selectedValue !== undefined) {
						dropDown.assignValue(editor, arg.selectedValue, arg);
					}
				}
			}
		});
	}
}

coke.mergeParameter = function(action, newParameter) {
	var oldParameter = action.get("parameter");
	if (!oldParameter) {
		action.set("parameter", {});
		oldParameter = action.get("parameter");
	}
	if (oldParameter instanceof dorado.util.Map) {
		oldParameter.put(newParameter)
	} else {
		dorado.Object.apply(oldParameter, newParameter);
	}
}

function parseParam(param) {
	if (!param)
		return null;

	var pairs = [];
	param.split(';').each(function(pair) {
		var i = pair.indexOf('='), key, value;
		if (i >= 0 && i < (pair.length - 1)) {
			pairs.push({
				key: pair.substring(0, i),
				value: pair.substring(i + 1)
			});
		} else {
			pairs.push({
				value: pair
			});
		}
	});
	return pairs;
}

function getEditor(entity, editorType) {
	if (!editorType) {
		return null;
	}
	var param;

	var editor = eval("new dorado.widget." + editorType + "()");
	if (editor instanceof dorado.widget.TextEditor) {
		if (param) {
			var trigger;
			if (param == "date") {
				trigger = "defaultDateDropDown";
			} else if (param == "dateTime") {
				trigger = "defaultDateTimeDropDown";
			} else {
				trigger = new dorado.widget.ListDropDown({
					items: parseParam(param),
					property: "value"
				});
			}
			editor.set("trigger", trigger);
		}
	} else if (editor instanceof dorado.widget.CheckBox) {
		editor.set("exClassName", "d-checkbox-center")
	} else if (editor instanceof dorado.widget.RadioGroup) {
		editor.set("layout", "flow");
		if (param) {
			var radioButtons = [];
			parseParam(param).each(function(pair) {
				radioButtons.push({
					value: pair.key,
					text: pair.value
				});
			});
			editor.set("radioButtons", radioButtons);
		}
	} else if (editor instanceof dorado.widget.DateTimeSpinner) {
		editor.set({
			type: param
		});
	} else if (editor instanceof dorado.widget.CustomSpinner) {
		editor.set("pattern", param);
	}
	return editor;
}

if (dorado && dorado.widget && dorado.widget.grid) {
	var CellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer, {
		$className: "org.xobo.dorado.widget.CellRenderer",
		property: null,
		editorType: null,
		createSubControl: function(arg) {
			var self = this;
			var entity = arg.data;
			var editor = getEditor(entity, this.editorType);
			if (editor) {
				editor.set("width", "100%");
			}
			return editor;
		},
		refreshSubControl: function(editor, arg) {
			if (editor)
				editor.set("value", arg.data.get(this.property));
		}
	});
}


dorado.widget.View.registerDefaultComponent("defaultDateDropDown", function() {
	return new dorado.widget.DateDropDown({ autoOpen: true });
});

dorado.afterInit(function() {
	jQuery(document).mousemove(function(event) {
		if (event.clientX < 2 && top.viewMain) {
			var dialogMenu = top.viewMain.id("dialogMenu");
			if (dialogMenu) {
				dialogMenu.$show()
			};
		}
	})
});