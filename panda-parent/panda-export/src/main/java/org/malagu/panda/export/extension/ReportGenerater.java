package org.malagu.panda.export.extension;

import org.malagu.panda.export.excel.ExcelReportModelGenerater;
import org.springframework.stereotype.Component;

@Component(ReportGenerater.BEAN_ID)
public class ReportGenerater extends ExcelReportModelGenerater {

	public static final String BEAN_ID = "panda.ReportGenerater";

}
