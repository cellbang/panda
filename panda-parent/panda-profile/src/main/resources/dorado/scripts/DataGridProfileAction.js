(function(){
    dorado.widget.DataGridProfileAction = $extend(dorado.widget.ComponentProfileAction, {
        $className: "dorado.widget.DataGridProfileAction",
        ATTRIBUTES: {
            showLabelConfig: {
                defaultValue: true
            },
            
            showColSpanConfig: {
                defaultValue: true
            },
            
            showRowSpanConfig: {
                defaultValue: true
            },
            
            showVisibleConfig: {
                defaultValue: true
            },
            
            showHideModeConfig: {
                defaultValue: true
            },
            
            dataGrid: {
                setter: function(value, attr){
					var dg = value.view.id(value.component);
                    this.addToMenu(dg);
                    this["_" + attr] = value;
                }
            }
        },
        
        getCols: function(){
            return this.getComponent().get("fixedColumnCount");
        },
        
        getComponentId: function(){
            return this._dataGrid.component;
        },
        
        getView: function(){
            return this._dataGrid.view;
        },
        
        getMembers: function(){
            return this.buildDataGrid(this.getComponent());
        },
        
        buildDataGrid: function(component){
            var list = new Array();
            var columns = component.get("columns");
            for (var i = 0; i < columns.size; i++) {
                var column = columns.get(i);
                if (column instanceof dorado.widget.grid.DataColumn) {
                    this.buildDataColumnEntity(column, list, i);
                }
                else 
                    if (column instanceof dorado.widget.grid.ColumnGroup) {
                        this.saveColumnGroup(column, list, i);
                    }
            }
            return list;
        },
        
        saveColumnGroup: function(column, list, order, parentControl){
            var columns = column.get("columns");
            var c = null;
            for (var i = 0; i < columns.size; i++) {
                c = columns.get(i);
                if (c instanceof dorado.widget.grid.ColumnGroup) {
                    this.saveColumnGroup(c, list, i, column.get("name"));
                }
                else {
                    this.buildDataColumnEntity(c, list, i, column.get("name"));
                }
            }
            this.buildGroupColumnEntity(column, list, order, parentControl);
        },
        
        buildDataColumnEntity: function(column, list, order, parentControl){
            var entity = {
                controlId: column.get("name"),
                controlName: column.get("name"),
                caption: column.get("caption"),
                visible: column.get("visible"),
                order: order
            };
            if (parentControl) {
                entity.parentControl = parentControl;
            }
            if (column instanceof dorado.widget.grid.IndicatorColumn) {
                entity.controlType = "IndicatorColumn";
            }
            else 
                if (column instanceof dorado.widget.grid.RowNumColumn) {
                    entity.controlType = "RowNumColumn";
                }
                else 
                    if (column instanceof dorado.widget.grid.RowSelectorColumn) {
                        entity.controlType = "RowSelectorColumn";
                    }
                    else {
                        entity.controlType = "DataColumn";
                        entity.width = column.get("width");
                    }
            list.push(entity);
        },
        
        buildGroupColumnEntity: function(column, list, order, parentControl){
            var entity = {
                controlId: column.get("name"),
                controlName: column.get("name"),
                controlType: "ColumnGroup",
                caption: column.get("caption"),
                visible: column.get("visible"),
                order: order
            };
            if (parentControl) {
                entity.parentControl = parentControl;
            }
            list.push(entity);
        },
        addToMenu: function(dataGrid){
            if (!dataGrid) {
                return;
            }
            var menu = dataGrid._grid.getHeaderOptionMenu(true);
			
            menu.addItem(new dorado.widget.menu.Separator({
                name: "fixSeprator"
            }));
			
			var self = this;
            menu.addItem({
                name: "save",
                caption: "保存样式",
                iconClass: "fa fa-floppy-o",
                onClick: function(){
					self.execute();
                }
            });
            menu.addItem({
                name: "reset",
                caption: "重置样式",
                iconClass: "fa fa-undo",
                onClick: function(){
					self.resetConfig();
                }
            });
        }
    });
})();
