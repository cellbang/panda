package org.malagu.panda.export.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.malagu.panda.export.utils.ExportUtils;
import org.malagu.panda.swfviewer.controller.PdfToSwfConverter;
import org.malagu.panda.swfviewer.handler.ISwfFileHandler;
import org.springframework.stereotype.Service;

@Service(ExportPdfToSwfHandler.BEAN_ID)
public class ExportPdfToSwfHandler implements ISwfFileHandler {

	public static final String BEAN_ID = "panda.ExportPdfToSwfHandler";

	public static final String NAME = "export.pdf2swf";

	public String getHandlerName() {
		return NAME;
	}

	public String getHandlerDesc() {
		return "导出的pdf报表生成swf文件在线预览";
	}

	public File execute(Map<String, Object> varMap) throws Exception {
		String id = (String) varMap.get("id");
		String name = (String) varMap.get("name");
		name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		if (!StringUtils.isNotEmpty(id) || !id.matches("[a-z0-9-]+")) {
			throw new IllegalArgumentException("illegal id");
		}
		if (!StringUtils.isNotEmpty(name) || !name.matches("[^/\\\\?<>*:\"|]+(\\.pdf)$")) {
			throw new IllegalArgumentException("illegal name");
		}
		File f = ExportUtils.getFile(id, name);
		if (!f.exists()) {
			throw new RuntimeException("文件不存在！" + ExportUtils.getFileStorePath() + id + "_" + name);
		}
		String sourcePdf = f.getAbsolutePath();
		PdfToSwfConverter pdfToSwfConverter = new PdfToSwfConverter();
		String swf = pdfToSwfConverter.execute(sourcePdf, null);
		return new File(swf);
	}
}
