(function(win) {
	var viewMap = {};

	function register(subwin, view) {
		var viewName = view.get("name");
		viewMap[viewName] = view;
		$(subwin).unload(function() {
			delete viewMap[viewName];
		});
	}

	function send(service, view, dataGrid) {
		var dataSet = dataGrid.get("dataSet.id");
		var dataPath = dataGrid.get("dataPath");
		var selections = dataGrid.get("selection");
		var result = [];
		selections.each(function(entity) {
			result.push(entity.toJSON());
		})
		var jsonData = dorado.JSON.stringify(result);
		console.log(jsonData);
		service = service || "openRest";

		top.coke.chatSocket.send("remote", {
			service : "openRest",
			data : {
				type : "CopyData",
				data : {
					userId : 10000,
					dataSet : dataSet,
					dataPath : dataPath,
					json : jsonData,
					view : view.get("name")
				}
			}
		})
	}

	function receive(message) {
		var dataSetId = message.data.dataSet;
		var dataPath = message.data.dataPath;
		var json = message.data.json;
		var viewName = message.data.view;

		var view = viewMap[viewName];
		var dataSet = view.id(dataSetId);
		if (json) {
			json = dorado.JSON.parse(json);
		}
		if (jQuery.isArray(json)) {
			for (var i = 0; i < json.length; i++) {
				var entity = dataSet.getData(dataPath).insert();
				entity.fromJSON(json[i]);
			}
		} else {
			// dataSet.getData(dataPath).insert(json);
		}
	}

	if (top.coke.webmessage) {
		top.coke.webmessage.addResolver("CopyData", receive);
	}

	win.coke = win.coke || {};

	win.coke.copydata = {
		register : register,
		send : send
	};
}(window))