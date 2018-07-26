(function(win) {
	var viewMap = {};

	function exportBase64() {
		var result = this.exportObject();
		var jsonData = dorado.JSON.stringify(result);
		return LZString.compressToBase64(jsonData);
	}
	
	function importBase64(data) {
		var dataSet = this.dataControl.get("dataSet");
		var dataPath = this.dataControl.get("dataPath");

		var object = dorado.JSON.parse(LZString.decompressFromBase64(data));
		this.importObject(object);
	}
	

	function exportObject() {
		var dataSet = this.dataControl.get("dataSet");
		var dataPath = this.dataControl.get("dataPath");

		var selections = [];
		if (this.dataControl instanceof dorado.widget.AbstractList) {
			selections = this.dataControl.get("selection");
		}
		var result = [];
		var self = this;
		if (selections.length) {
			selections.each(function(entity) {
				self.references.each(function(p){
					entity.get(p);
				});
				result.push(entity.toJSON());
			})
		} else {
			var list = dataSet.getData(dataPath);
			var currentEntity = list.current;
			if (currentEntity) {
				self.references.each(function(p){
					currentEntity.get(p);
				});
				result.push(currentEntity.toJSON());
			}
		}

		var self = this;
		if (this.ignoreProperties && jQuery.isFunction(this.ignoreProperties.each)) {
			this.ignoreProperties.each(function(property) {
				self.setData(result, property, null);
			});
		}
		return result;
	}
	
	function importObject(object) {
		var dataSet = this.dataControl.get("dataSet");
		var dataPath = this.dataControl.get("dataPath");

		if (jQuery.isFunction(object.each)) {
			var self = this;
			object.each(function(item) {
				var entity = dataSet.getData(dataPath).insert(item);
				self.setEntityState(entity, dorado.Entity.STATE_NEW);
			})
		} else {
			dataSet.getData(dataPath).insert(object);
		}
		if (jQuery.isFunction(this.importCallback)) {
			this.importCallback();
		}
	}

	function setEntityState(entity, state) {
		if (!(entity instanceof dorado.Entity)) {
			return;
		}
		entity.state = state;
		var json = entity.toJSON();
		for ( var p in json) {
			var value = entity.get(p);
			if (value instanceof dorado.Entity) {
				setEntityState(value, state);
			} else if (value instanceof dorado.EntityList) {
				value.each(function(item) {
					setEntityState(item, state);
				});
			}
		}
	}



	function setData(data, property, value) {
		var properties = property.split(".");

		if (properties.length > 1) {
			if (jQuery.isFunction(data.each)) {
				var self = this;
				data.each(function(item) {
					if (!item[properties[0]]) {
						item[properties[0]] = {};
					}
					self.setData(item[properties[0]], properties.slice(1).join("."), value);
				})
			} else {
				if (!data[properties[0]]) {
					data[properties[0]] = {};
				}
				setData(data[properties[0]], properties.slice(1).join("."), value);
			}
		} else {
			if (jQuery.isFunction(data.each)) {
				data.each(function(item) {
					item[property] = value;
				})
			} else {
				data[property] = value;
			}
		}
	}

	win.coke = win.coke || {};

	win.coke.DataTransfer = function(dataControl, ignoreProperties, importCallback, references) {
		this.dataControl = dataControl;
		if (typeof ignoreProperties == "string") {
			this.ignoreProperties = ignoreProperties.split(",");
		}
		
		if (typeof references == "string"){
			this.references = references.split(",");
		}
		this.importCallback = importCallback;
	}
	var prototype = win.coke.DataTransfer.prototype;
	prototype.exportBase64 = exportBase64;
	prototype.importBase64 = importBase64;
	prototype.exportObject = exportObject;
	prototype.importObject = importObject;
	prototype.setData = setData;
	prototype.setEntityState = setEntityState;

}(window))