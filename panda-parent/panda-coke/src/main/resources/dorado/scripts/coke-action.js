$namespace("org.malagu.panda.coke");
org.malagu.panda.coke.renderDataGridError = function(arg, errorColor) {
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

org.malagu.panda.coke.smartExecute = function(action, parameter, executeCallback, cacheCallback) {
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
function $xa_insertItem(dataSet, dataPath, dialog, data) {
	if (!data) {
		data = {
			xybz : true,
			yxbz : true
		};
	}
	var list = dataSet.getData(dataPath);
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

function $xa_insertChildItem(dataTree, childrenName, dialog, data) {
	childrenName = childrenName || "children";
	data = data || {};

	var currentEntity = dataTree.get("currentEntity");
	if (currentEntity) {
		dataTree.get("currentNode").expand();
		// newEntity = currentEntity.createChild(childrenName, data);
		newEntity = currentEntity.get("children").insert(data);
		dataTree.set("currentEntity", newEntity);
		setTimeout(function() {
			dataTree.set("currentEntity", newEntity);
		}, 200);
		if (dialog) {
			dialog.show();
		}
	}
}

function $xa_isItemEditable(toolbarEditId) {
	if (toolbarEditId) {
		var toolbarEdit = view.id(toolbarEditId);
		return toolbarEdit && !toolbarEdit.get("disabled");
	} else {
		return true;
	}
}

function $xa_editItem(dataSet, dataPath, dialog, toolbarEditId) {
	if ($xa_isItemEditable(toolbarEditId)) {
		var entity = dataSet.getData(dataPath);
		if (entity) {
			dialog.show();
		} else {
			dorado.widget.NotifyTipManager.notify("没有可编辑的记录!");
		}
	}
}

function $xa_deleteItem(dataSet, dataPath, updateAction, callBack) {
	var entity = dataSet.getData(dataPath);
	if (entity) {
		dorado.MessageBox.confirm("确认要删除选中的记录么？", {
			icon : "WARNING",
			title : "删除记录",
			callback : function() {
				entity.remove();
				updateAction.execute(callBack);
			}
		});
	} else {
		dorado.widget.NotifyTipManager.notify('没有可删除的记录。');
	}
}

function $xa_deleteItems(dataGrid, updateAction, config) {
	var callBack;
	if (!config) {
		config = {};
	}
	if (jQuery.isFunction(config)) {
		config.callBack = config;
	}

	var selection = dataGrid.get("selection");
	if (!selection || selection.length == 0) {
		var list = dataGrid.get("dataSet").getData(dataGrid.get("dataPath"));
		if (list && list.current) {
			selection = [ list.current ];
		}
	}

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
		icon : "WARNING",
		title : "删除记录",
		callback : function() {
			selection.each(function(item) {
				item.remove();
			});
			updateAction && updateAction.execute(callBack);
		}
	});
}

function $xa_dialogSaveItem(dataSet, dataPath, updateAction, dialog, callback) {
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

function $xa_dialogCancelItem(dataSet, dataPath, dialog) {
	var entity = dataSet.getData(dataPath);
	if (entity && entity.isDirty()) {
		dorado.MessageBox.confirm("确认放弃当前修改？", {
			title : "关闭编辑窗口",
			callback : function() {
				entity.cancel();
				dialog && dialog.hide();
			}
		});
	} else {
		dialog && dialog.hide();
	}
}

function $xa_queryItem(dataSet, autoformQuery, dataPath) {
	dataSet.set("parameter", autoformQuery.get("entity").toJSON()).flushAsync();
}

function $xa_queryReferenceItem(entity, reference, autoformQuery) {
	var queryJson = autoformQuery.get("entity").toJSON();
	var parameter = reference.get("parameter");
	var lastQueryJson = autoformQuery.lastQueryJson;
	if (lastQueryJson) {
		for ( var p in lastQueryJson) {
			parameter.remove(p);
		}
	}
	parameter.put(queryJson);
	entity.reset(reference.get("name"));
	autoformQuery.lastQueryJson = queryJson;
}

function $xa_resetQueryform(dataSetQuery, config) {
	if (!config) {
		config = {};
	}
	dataSetQuery.set("data", config);
}

function $xa_getSelections(dataGrid, type) {
	if (!type) {
		type = "current";
	}

	var selection = dataGrid.get("selection");
	var list;
	if (selection && selection.length > 0) {
		list = selection;
	} else if (type == "all") {
		list = dataGrid.get("dataSet").getData(dataGrid.get("dataPath"));
	} else if (type == "current") {
		var current = dataGrid.get("dataSet").getData().current;
		list = [];
		list.push(current);
	} else {
		list = [];
	}
	return list;
}

function $xa_openUrl(link, title) {
	if (top.openUrlInFrameTab) {
		top.openUrlInFrameTab(link, title);
	} else {
		window.open(link, "_blank");
	}
}

function $xa_dataRowClick(rowList, clickCallback, doubleClickCallback) {
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

function travelObject(parent, property, callback) {
	var value;
	if (property || property === 0) {
		value = parent[property];
	} else {
		value = parent;
	}
	if (jQuery.isPlainObject(value)) {
		for ( var p in value) {
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