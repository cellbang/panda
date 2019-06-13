/**
 * 
 */
package org.malagu.panda.importer.service.impl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.importer.model.ImporterSolution;
import org.malagu.panda.importer.model.MappingRule;
import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.policy.ExcelPolicy;
import org.malagu.panda.importer.policy.PostProcessPolicy;
import org.malagu.panda.importer.policy.impl.DefaultPostProcessPolicy;
import org.malagu.panda.importer.service.ExcelPolicyService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author xobo
 *
 */
@Service
public class ExcelPolicyServiceImpl implements ExcelPolicyService, ApplicationContextAware {

  /*
   * (non-Javadoc)
   * 
   * @see org.malagu.panda.importer.service.ExcelPolicyService#parse(java.io.InputStream,
   * java.util.Map)
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public boolean parse(InputStream inpuStream, Map<String, Object> parameter) {
    String name = MapUtils.getString(parameter, "filename");
    String importerSolutionId = MapUtils.getString(parameter, "importerSolutionId");
    int startRow = MapUtils.getInteger(parameter, "startRow", 1);
    long fileSize = MapUtils.getInteger(parameter, "fileSize", 0);
    for (ExcelPolicy excelPolicy : excelPolicies) {
      if (excelPolicy.support(name)) {
        Context context = excelPolicy.createContext();
        context.setImporterSolutionId(importerSolutionId);
        initContext(context);
        context.setInputStream(new BufferedInputStream(inpuStream));
        context.setStartRow(startRow);
        context.setFileName(name);
        context.setFileSize(fileSize);
        context.setImporterSolutionId(importerSolutionId);
        context.setParams(parameter);
        try {
          excelPolicy.apply(context);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      }
    }
    return false;
  }

  protected void initContext(Context context)  {
    ImporterSolution importerSolution = getImporterSolution(context.getImporterSolutionId());
    Class<?> entityClass = null;
    try {
      entityClass = this.classLoader.loadClass(importerSolution.getEntityClassName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    List<MappingRule> mappingRules = importerSolution.getMappingRules();
    Assert.notEmpty(mappingRules, "mappingRules can not be empty.");

    context.setImporterSolution(importerSolution);
    context.setMappingRules(mappingRules);
    context.setEntityClass(entityClass);

    String postProcessPolicyBeanId = importerSolution.getPostProcessBean();
    if (StringUtils.isEmpty(postProcessPolicyBeanId)) {
      postProcessPolicyBeanId = DefaultPostProcessPolicy.BEAN_ID;
    }
    context.setPostProcessPolicy(
        (PostProcessPolicy) applicationContext.getBean(postProcessPolicyBeanId));
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

  private ClassLoader classLoader;
  private ApplicationContext applicationContext;

  @SuppressWarnings("rawtypes")
  @Autowired
  private Collection<ExcelPolicy> excelPolicies;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.classLoader = applicationContext.getClassLoader();
  }
}
