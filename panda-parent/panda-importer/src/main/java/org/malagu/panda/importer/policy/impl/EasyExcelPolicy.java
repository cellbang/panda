/**
 * 
 */
package org.malagu.panda.importer.policy.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.malagu.panda.importer.model.Cell;
import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.policy.ExcelPolicy;
import org.malagu.panda.importer.policy.ParseRecordPolicy;
import org.malagu.panda.importer.policy.XSSFContext;

import com.alibaba.excel.EasyExcel;

/**
 * @author xobo
 *
 */
public class EasyExcelPolicy implements ExcelPolicy<Context> {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.malagu.panda.importer.policy.ExcelPolicy#apply(org.malagu.panda.importer.policy.Context)
   */
  @SuppressWarnings("unchecked")
  @Override
  public void apply(Context context) throws Exception {

    List<Object> dataList = EasyExcel.read(context.getInputStream()).sheet(0)
        .headRowNumber(context.getStartRow() - 1).doReadSync();
    for (int i = 0; i < dataList.size(); i++) {
      Object object = dataList.get(i);

      // 旧版Excel返回List，新版返回Map
      if (object instanceof List) {
        List<Object> rowDataList = (List<Object>) object;
        for (int j = 0; j < rowDataList.size(); j++) {
          Object rowData = rowDataList.get(j);
          Cell cell = new Cell(i, j);
          cell.setValue(Objects.toString(rowData, null));
          context.addCell(cell);
        }
      } else if (object instanceof Map) {
        Map<Integer, Object> dataMap = (Map<Integer, Object>) object;
        for (Entry<Integer, Object> entry : dataMap.entrySet()) {
          Cell cell = new Cell(i, entry.getKey());
          cell.setValue(Objects.toString(entry.getValue(), null));
          context.addCell(cell);
        }
      }
    }

    parseRecordPolicy.apply(context);
    context.getPostProcessPolicy().apply(context);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.importer.policy.ExcelPolicy#createContext()
   */
  @Override
  public Context createContext() {
    return new XSSFContext();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.importer.policy.ExcelPolicy#support(java.lang.String)
   */
  @Override
  public boolean support(String fileName) {
    return true;
  }

  private ParseRecordPolicy parseRecordPolicy;

  public ParseRecordPolicy getParseRecordPolicy() {
    return parseRecordPolicy;
  }

  public void setParseRecordPolicy(ParseRecordPolicy parseRecordPolicy) {
    this.parseRecordPolicy = parseRecordPolicy;
  }


}
