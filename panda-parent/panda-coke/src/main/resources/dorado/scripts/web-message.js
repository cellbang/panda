(function(win) {
	var stack_bottomright = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};

	function sendDesktopNotification(title, content) {
		if (notify && notify.isSupported) {
			var requestPermission = notify.permissionLevel();
			if (requestPermission == "default") {
				notify.requestPermission(function(requestPermission) {
					if (requestPermission == "granted") {
						sendDesktopNotification(title, content);
					}
				});
			} else if (requestPermission == "granted") {
				notify.createNotification(title, {
					body : content,
					icon : "images/logo-png.png"
				})
			} else if (requestPermission == "denied") {
			}
		}
	}

	function sendNotify(title, content, link, ids) {
		var linkVisibility = link ? "visible" : "hidden";
		if (window.PNotify) {
			var opts = {
				title : title,
				text : content,
				addclass : "stack-bottomright",
				stack : stack_bottomright,
				confirm : {
					confirm : true,
					buttons : [
							{
								text : "已读",
								click : function(notice) {
									var ajaxAction = $id("viewMain").objects[0].id("ajaxActionBulkUpdate");
									if (ajaxAction && ids) {
										ajaxAction.set("parameter", {
											ids : ids,
											property : "unread",
											value : false
										}).execute(function() {
											if (notify) {
												notify.hide();
											}
										});
									} else {
										if (notify) {
											notify.hide();
										}
									}
								}
							},
							{
								text : "处理",
								addClass : 'btn-primary',
								click : function(notice) {
									if (top.openUrlInFrameTab) {
										top.openUrlInFrameTab(link, title);
									} else {
										win.open(link, '_blank');
									}
									notice.remove();
								}
							}, null ]
				}
			};
			new PNotify(opts);

		} else {

			var notify = dorado.widget.NotifyTipManager.notify({
				caption : title,
				content : {
					tagName : "div",
					content : [ {
						tagName : "p",
						content : content
					}, {
						tagName : "button",
						content : "已读",
						onclick : function() {
							var ajaxAction = $id("viewMain").objects[0].id("ajaxActionBulkUpdate");
							if (ajaxAction && ids) {
								ajaxAction.set("parameter", {
									ids : ids,
									property : "unread",
									value : false
								}).execute(function() {
									if (notify) {
										notify.hide();
									}
								});
							} else {
								if (notify) {
									notify.hide();
								}
							}
						}
					}, {
						tagName : "button",
						content : "处理",
						style : {
							visibility : linkVisibility
						},
						onclick : function() {
							if (top.openUrlInFrameTab) {
								top.openUrlInFrameTab(link, title);
							} else {
								win.open(link, '_blank');
							}
						}
					} ]
				}
			});
		}
	}

	var messageResolvers = {};
	function addResolver(type, resolver) {
		var resolvers = messageResolvers[type];
		if (!resolvers) {
			resolvers = [];
			messageResolvers[type] = resolvers;
		}
		resolvers.push(resolver);
	}

	function getMessageResolvers() {
		return messageResolvers;
	}

	function removeResolver(type, resolver) {
		var resolvers = messageResolvers[type];
		if (!resolvers) {
			resolvers = [];
		}
		var i = resolvers.indexOf(resolver);
		if (i != -1) {
			resolvers.splice(i, 1);
		}
	}

	function processMessage(message) {
		console.log(message);
		if (!message || !message.type) {
			return;
		}
		var resolvers = messageResolvers[message.type];
		if (!resolvers || !resolvers.length) {
			return;
		}
		for (var i = 0; i < resolvers.length; i++) {
			var resovler = resolvers[i];
			if (jQuery.isFunction(resovler)) {
				resovler(message);
			}
		}
	}
	win.coke = win.coke || {};
	win.coke.webmessage = {
		sendDesktopNotification : sendDesktopNotification,
		sendNotify : sendNotify,
		addResolver : addResolver,
		removeResolver : removeResolver,
		processMessage : processMessage,
		getMessageResolvers : getMessageResolvers
	};
	if (jQuery.isFunction(Object.seal)) {
		Object.seal(win.coke.webmessage);
	}
}(window));

coke.webmessage.addResolver("OpenLink", function(message) {
	if (jQuery.isFunction(top.openUrlInFrameTab)) {
		openUrlInFrameTab(data.link, data.title);
	}
});

coke.webmessage.addResolver("OnlineMessage", function(message) {
	var data = message.data;
	var title = data.title;
	var content = data.content;
	var link = data.link;
	var ids = [];
	ids.push(data.id);
	if (content) {
		coke.webmessage.sendDesktopNotification(title, content);
		coke.webmessage.sendNotify(title, content, link, ids);
	}
});
