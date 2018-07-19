package org.malagu.panda.importexcel.policy.impl;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.importexcel.model.ImporterSolution;
import org.malagu.panda.importexcel.model.MappingRule;
import org.malagu.panda.importexcel.policy.Context;
import org.malagu.panda.importexcel.policy.ExcelPolicy;
import org.malagu.panda.importexcel.policy.ParseRecordPolicy;
import org.malagu.panda.importexcel.policy.SheetPolicy;
import org.malagu.panda.importexcel.policy.XSSFContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 *@author Kevin.yang
 *@since 2015年8月22日
 */
public class XSSFExcelPolicy implements ExcelPolicy<XSSFContext>, ApplicationContextAware {
 
	private SheetPolicy<XSSFContext> sheetPolicy;
	
	private ParseRecordPolicy parseRecordPolicy;
	
	private ClassLoader classLoader;
	
	@Override
	public void apply(XSSFContext context) throws Exception {
        OPCPackage xlsxPackage = OPCPackage.open(context.getInpuStream());
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        context.setStyles(xssfReader.getStylesTable());
        context.setStrings(new ReadOnlySharedStringsTable(xlsxPackage));
        
        initContext(context);
        
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader
                .getSheetsData();
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            context.setInpuStream(stream);
            String sheetName = context.getImporterSolution().getExcelSheetName();
            if (StringUtils.isNotEmpty(sheetName)) {
				if (sheetName.equals(iter.getSheetName())) {
					sheetPolicy.apply(context);
					break;
				}
			} else {
	            sheetPolicy.apply(context);
			}
            stream.close();
        }
        
        parseRecordPolicy.apply(context);
        
	}
	
	protected void initContext(Context context) throws ClassNotFoundException {
		ImporterSolution importerSolution = getImporterSolution(context.getImporterSolutionId());
		Class<?> entityClass =  this.classLoader.loadClass(importerSolution.getEntityClassName());
		List<MappingRule> mappingRules = importerSolution.getMappingRules();
		Assert.notEmpty(mappingRules, "mappingRules can not be empty.");
		
		context.setImporterSolution(importerSolution);
		context.setMappingRules(mappingRules);
		context.setEntityClass(entityClass);
	}
	
	private ImporterSolution getImporterSolution(String importerSolutionId) {
		ImporterSolution importerSolution = JpaUtil.getOne(ImporterSolution.class, importerSolutionId);
		List<MappingRule> mappingRules = JpaUtil
				.linq(MappingRule.class)
				.equal("importerSolutionId", importerSolutionId)
				.list();
		importerSolution.setMappingRules(mappingRules);
		return importerSolution;
		
	}

	@Override
	public boolean support(String fileName) {
		return fileName.endsWith(".xlsx");
	}

	public void setSheetPolicy(SheetPolicy<XSSFContext> sheetPolicy) {
		this.sheetPolicy = sheetPolicy;
	}

	@Override
	public XSSFContext createContext() {
		return new XSSFContext();
	}

	public void setParseRecordPolicy(ParseRecordPolicy parseRecordPolicy) {
		this.parseRecordPolicy = parseRecordPolicy;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.classLoader = applicationContext.getClassLoader();
		
	}

}
