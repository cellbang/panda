package org.malagu.panda.importer.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.malagu.panda.coke.utility.ExcelReaderUtils;
import org.malagu.panda.coke.utility.JSONUtil;
import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.policy.ExcelPolicy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExcelUploadFileProcessor implements ApplicationContextAware {


  private Collection<ExcelPolicy> excelPolicies;

  @FileResolver
  @Transactional
  public String upload(UploadFile file, Map<String, Object> parameter) throws Exception {
    MultipartFile multipartFile = file.getMultipartFile();
    String name = multipartFile.getOriginalFilename();
    String importerSolutionId = (String) parameter.get("importerSolutionId");
    int startRow = 1;
    if (parameter.get("startRow") != null) {
      startRow = Integer.parseInt(parameter.get("startRow").toString());
    }
    Assert.hasLength(importerSolutionId, "Excel导入方案编码必须配置。");

    InputStream inpuStream = multipartFile.getInputStream();
    try {
      for (ExcelPolicy excelPolicy : excelPolicies) {
        if (excelPolicy.support(name)) {
          Context context = excelPolicy.createContext();
          context.setInputStream(inpuStream);
          context.setStartRow(startRow);
          context.setFileName(name);
          context.setFileSize(file.getSize());
          context.setImporterSolutionId(importerSolutionId);
          context.setParams(parameter);
          excelPolicy.apply(context);
          break;
        }
      }
    } finally {
      IOUtils.closeQuietly(inpuStream);
    }
    return null;
  }

  @FileResolver
  @Transactional
  public String uploadTemplate(UploadFile file, Map<String, Object> parameter) throws Exception {
    MultipartFile multipartFile = file.getMultipartFile();
    String name = multipartFile.getOriginalFilename();
    int startRow = 0;
    if (parameter.get("startRow") != null) {
      startRow = Integer.parseInt(parameter.get("startRow").toString());
    }

    List<List<String>> result = null;
    try (InputStream inpuStream = multipartFile.getInputStream()) {
      result = ExcelReaderUtils.toList(name, inpuStream, 0, startRow, startRow + 1);
    }
    if (result.isEmpty()) {
      return null;
    }

    List<Map<String, Object>> listMap = new ArrayList<>();
    List<String> row = result.get(0);
    List<String> valueRow = result.size() > 1 ? result.get(1) : null;
    for (int i = 0; i < row.size(); i++) {
      Map<String, Object> map = new HashMap<>();
      map.put("index", i);
      map.put("label", row.get(i));
      map.put("value", valueRow.get(i));
      listMap.add(map);
    }

    return JSONUtil.toJSON(listMap);
  }



  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    excelPolicies = applicationContext.getBeansOfType(ExcelPolicy.class).values();
  }
}
