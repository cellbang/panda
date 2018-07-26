package org.malagu.panda.coke.utility;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.view.widget.grid.IndicatorColumn;
import com.bstek.dorado.view.widget.grid.RowNumColumn;
import com.bstek.dorado.view.widget.grid.RowSelectorColumn;

public class DataGridUtil {

  public static Map<String, Column> extractColumns(DataGrid dataGrid) {
    Map<String, Column> columnsMap = new LinkedHashMap<String, Column>();
    convertDataGridColumnsToMap(dataGrid.getColumns(), columnsMap);
    return columnsMap;
  }

  /**
   * 获取系统默认DataGrid的所有列，包括ColumnGroup中的多有列到Map对象中
   * 
   * @param topColumns
   * @param groupColumns
   */
  private static void convertDataGridColumnsToMap(List<Column> topColumns,
      Map<String, Column> groupColumns) {
    for (Column column : topColumns) {
      if (column instanceof ColumnGroup) {
        convertDataGridColumnsToMap(((ColumnGroup) column).getColumns(), groupColumns);
      }
      String name = column.getName();
      if (StringUtils.isNotEmpty(name)) { // DataColumn must have a name;
        groupColumns.put(name, column);
      } else {
        if (column instanceof IndicatorColumn) {
          name = "Indicator";
        } else if (column instanceof RowNumColumn) {
          name = "RowNum";
        } else if (column instanceof RowSelectorColumn) {
          name = "RowSelector";
        }
        if (StringUtils.isNotEmpty(name)) {
          groupColumns.put(name, column);
        }
      }
    }
  }
}
