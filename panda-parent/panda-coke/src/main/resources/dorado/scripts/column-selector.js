dorado.widget.grid.RowSelectorColumn = $extend(dorado.widget.grid.DataColumn, /** @scope dorado.widget.grid.RowSelectorColumn.prototype */
{
	ATTRIBUTES : /** @scope dorado.widget.grid.RowSelectorColumn.prototype */
	{
		width : {
			defaultValue : 16
		},
		dynaRowHeight : {
			defaultValue : false
		},
		align : {
			defaultValue : "center"
		},
		caption : {
			defaultValue : "RowSelector"
		},
		property : {
			defaultValue : "none"
		},
		resizeable : {
			defaultValue : false
		},
		filterable : {
			defaultValue : false
		},
		headerRenderer : {
			dontEvalDefaultValue : true,
			defaultValue : function(dom, arg) {

				function getMenu(column) {
					var menu = column._rowSelectorMenu;
					if (!menu) {
						menu = column._rowSelectorMenu = new dorado.widget.Menu({
							items : [ {
								name : "select-all",
								caption : $resource("dorado.grid.SelectAll"),
								icon : $url("icon/select-all.png"),
								onClick : function(self) {
									grid.selectAll();
								}
							}, {
								name : "unselect-all",
								caption : $resource("dorado.grid.UnselectAll"),
								icon : $url("icon/select-all.png"),
								onClick : function(self) {
									grid.unselectAll();
								}
							} ]
						});
						grid.registerInnerControl(menu);
					}
					return menu;
				}

				var grid = arg.grid, column = arg.column, cell = dom.parentNode;
				$fly(dom).empty();

				var $cell = $fly(cell);
				$cell.addClass("row-selector");
				if (!$cell.data("selectionMenuBinded")) {
					$cell.data("selectionMenuBinded", true).click(function() {
						if (grid._selectionMode == "multiRows") {
							var menu = getMenu(column);
							menu.show({
								anchorTarget : cell,
								align : "innerright",
								vAlign : "bottom"
							});
						}
						return false;
					});
				}
			}
		},
		renderer : {
			defaultValue : function() {
				return new dorado.widget.grid.RowSelectorCellRenderer();
			}
		}
	}
});