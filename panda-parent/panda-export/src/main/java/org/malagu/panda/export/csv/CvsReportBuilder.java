package org.malagu.panda.export.csv;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.export.extension.ReportBuilder;
import org.malagu.panda.export.model.FileExtension;
import org.malagu.panda.export.model.ReportGrid;
import org.malagu.panda.export.model.ReportGridHeader;
import org.springframework.stereotype.Component;

import com.bstek.dorado.core.Configure;

/**
 * 导出CSV格式文件默认实现
 * 
 */
@Component(CvsReportBuilder.BEAN_ID)
public class CvsReportBuilder implements ReportBuilder {

	public static final String BEAN_ID = "panda.CvsReportBuilder";

	/**
	 * 分隔符
	 */
	public String delimiter = Configure.getString("panda.export.csv.delimiter");
	/**
	 * 单元格包裹符号
	 */
	public String cellWrapSymbol = Configure.getString("panda.export.csv.cellWrapSymbol");
	/**
	 * 字符集
	 */
	public String charset = Configure.getString("panda.export.csv.charset");

	
	public void execute(OutputStream out, ReportGrid reportGrid) throws Exception {
		this.fillData(out, reportGrid);
	}
	
	private String getLineSeparator(){
		return System.getProperty("line.separator");
	}

	public boolean support(String extension) {
		return extension.equals(FileExtension.csv) ? true : false;
	}

	public void fillData(OutputStream out, ReportGrid gridModel) throws Exception {
		List<ReportGridHeader> headers = gridModel.getColumnHeaders();
		List<Map<String, Object>> datas = gridModel.getGridDataModel().getDatas();
		StringBuffer rowStr = null;
		//output header
		if (gridModel.isShowHeader()){
			rowStr = new StringBuffer();
			int cellIndex = -1;
			for (ReportGridHeader header : headers) {
				cellIndex++;
				String value = header.getLabel();
				if (StringUtils.isEmpty(value)){
					value = header.getColumnName();
				}
				if (value.indexOf(delimiter)>-1){
					value = StringUtils.replace(value, delimiter, " ");
				}
				rowStr.append(cellWrapSymbol).append(value).append(cellWrapSymbol);
				if (cellIndex == headers.size() - 1) {
					rowStr.append(getLineSeparator());
				} else {
					rowStr.append(delimiter);
				}
			}
			out.write(rowStr.toString().getBytes(charset));
		}
		//output data
		for (Map<String, Object> currentRowData : datas) {
			rowStr = new StringBuffer();
			int cellIndex = -1;
			for (ReportGridHeader header : headers) {
				cellIndex++;
				Object value = currentRowData.get(header.getColumnName());
				rowStr.append(cellWrapSymbol).append(value == null ? "" : value.toString()).append(cellWrapSymbol);
				if (cellIndex == headers.size() - 1) {
					rowStr.append(getLineSeparator());
				} else {
					rowStr.append(delimiter);
				}
			}
			out.write(rowStr.toString().getBytes(charset));
		}
	}

}
