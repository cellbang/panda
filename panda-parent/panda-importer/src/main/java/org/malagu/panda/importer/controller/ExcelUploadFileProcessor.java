package org.malagu.panda.importer.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.malagu.panda.importer.service.ExcelPolicyService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.xobo.toolkit.ExcelReaderUtil;
import org.xobo.toolkit.JSONUtil;
import org.xobo.toolkit.model.ExcelHeader;

import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

public class ExcelUploadFileProcessor implements ApplicationContextAware {

  @Autowired
  private ExcelPolicyService excelPolicyService;

  @FileResolver
  @Transactional
  public String upload(UploadFile file, Map<String, Object> parameter) throws Exception {
    int startRow = MapUtils.getIntValue(parameter, "startRow");

    MultipartFile multipartFile = file.getMultipartFile();
    String name = multipartFile.getOriginalFilename();
    String importerSolutionId = (String) parameter.get("importerSolutionId");
    Assert.hasLength(importerSolutionId, "Excel导入方案编码必须配置。");

    File tempFile = File.createTempFile(name, null);
    multipartFile.transferTo(tempFile);

    List<ExcelHeader> excelHeaderList = ExcelReaderUtil.loadExcelHeadList(tempFile, startRow);
    parameter.put(ExcelPolicyService.EXCEL_HEADER_LIST, excelHeaderList);

    try (InputStream inputStream = new BufferedInputStream(new FileInputStream(tempFile))) {
      parameter.put("filename", name);
      parameter.put("fileSize", file.getSize());
      excelPolicyService.parse(inputStream, parameter);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @FileResolver
  @Transactional
  public Object uploadTemplate(UploadFile file, Map<String, Object> parameter) throws Exception {
    MultipartFile multipartFile = file.getMultipartFile();
    int startRow = MapUtils.getIntValue(parameter, "startRow");

    List<ExcelHeader> excelHeaderList = null;
    try {
      excelHeaderList = ExcelReaderUtil.loadExcelHeadList(multipartFile.getInputStream(), startRow);
    } finally {
      IOUtils.closeQuietly(multipartFile.getInputStream());
    }

    return Base64.getEncoder().encodeToString(JSONUtil.toJSON(excelHeaderList).getBytes("UTF-8"));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {}
}
