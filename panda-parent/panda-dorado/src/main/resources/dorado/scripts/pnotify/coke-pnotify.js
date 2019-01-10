(function(win){
    PNotify.prototype.options.buttons.labels = {
        close: "关闭",
        stick: "钉",
        unstick: "反钉"
    };
    
    var stack_topleft = {
        "dir1": "down",
        "dir2": "right",
        "push": "top"
    };
    var stack_bottomleft = {
        "dir1": "right",
        "dir2": "up",
        "push": "top"
    };
    var stack_custom = {
        "dir1": "right",
        "dir2": "down"
    };
    var stack_custom2 = {
        "dir1": "left",
        "dir2": "up",
        "push": "top"
    };
    var stack_modal = {
        "dir1": "down",
        "dir2": "right",
        "push": "top",
        "modal": true,
        "overlay_close": true
    };
    var stack_bar_top = {
        "dir1": "down",
        "dir2": "right",
        "push": "top",
        "spacing1": 0,
        "spacing2": 0
    };
    var stack_bar_bottom = {
        "dir1": "up",
        "dir2": "right",
        "spacing1": 0,
        "spacing2": 0
    };
    var stack_bottomright = {
        "dir1": "up",
        "dir2": "left",
        "firstpos1": 25,
        "firstpos2": 25
    };
    
    function notify(title, content,type){
        var opts = {
            title: title,
            text: content,
            addclass: "stack-bottomright",
            cornerclass: "",
			type: type,
            stack: stack_bottomright
        };
        new PNotify(opts);
    }
    function notifyLeftTop(title, content,type){
        var opts = {
            title: title,
            text: content,
            cornerclass: "",
			type: type,
            stack: stack_topleft
        };
        new PNotify(opts);
    }
    function notifyBarBottom(title, content){
        var opts = {
            title: title,
            text: content,
            addclass: "stack-bar-bottom",
            cornerclass: "",
            width: "70%",
            stack: stack_bar_bottom
        };
        new PNotify(opts);
    }
    
    win.coke = win.coke || {};
    win.coke.notify = {
        notify: notify,
		notifyLeftTop: notifyLeftTop,
        notifyBarBottom: notifyBarBottom
    }
})(window);
