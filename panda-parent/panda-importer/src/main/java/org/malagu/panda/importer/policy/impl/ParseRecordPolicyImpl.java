package org.malagu.panda.importer.policy.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.malagu.panda.dorado.linq.BeanUtils;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.importer.model.Cell;
import org.malagu.panda.importer.model.MappingRule;
import org.malagu.panda.importer.model.Record;
import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.policy.ParseRecordPolicy;
import org.malagu.panda.importer.processor.CellPostprocessor;
import org.malagu.panda.importer.processor.CellPreprocessor;
import org.malagu.panda.importer.processor.CellProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import net.sf.cglib.beans.BeanMap;

/**
 * @author Kevin.yang
 * @since 2015年8月23日
 */
public class ParseRecordPolicyImpl implements ParseRecordPolicy, ApplicationContextAware {

  private static Logger logger = LoggerFactory.getLogger(ParseRecordPolicyImpl.class);

  private Collection<CellPreprocessor> cellPreprocessors;
  private Collection<CellProcessor> cellProcessors;
  private Collection<CellPostprocessor> cellPostprocessors;

  @Override
  public void apply(Context context) throws ClassNotFoundException {
    List<Record> records = context.getRecords();

    List<Object> resultList = new ArrayList<>(records.size());
    context.setResultList(resultList);

    for (int i = 0; i < records.size(); i++) {
      Record record = records.get(i);
      Object entity = BeanUtils.newInstance(context.getEntityClass());
      context.setCurrentEntity(entity);
      context.setCurrentRecord(record);
      try {
        String idProperty = JpaUtil.getIdName(context.getEntityClass());
        BeanMap beanMap = BeanMap.create(context.getCurrentEntity());
        if (beanMap.getPropertyType(idProperty) == String.class) {
          beanMap.put(idProperty, UUID.randomUUID().toString());
        }
      } catch (IllegalArgumentException e) {
        logger.info("cannot get Id of " + context.getEntityClass().getName());
      }
      for (MappingRule mappingRule : context.getMappingRules()) {
        Cell cell = record.getCell(mappingRule.getExcelColumn());
        context.setCurrentMappingRule(mappingRule);
        context.setCurrentCell(cell);
        cellPreprocess(context);
        cellProcess(context);
        cellPostprocess(context);
      }
      resultList.add(entity);

    }

  }


  protected void cellPreprocess(Context context) {
    for (CellPreprocessor cellPreProcessor : cellPreprocessors) {
      if (cellPreProcessor.support(context)) {
        cellPreProcessor.process(context);
      }
    }
  }

  protected void cellProcess(Context context) {
    for (CellProcessor cellProcessor : cellProcessors) {
      if (cellProcessor.support(context)) {
        cellProcessor.process(context);
      }
    }
  }

  protected void cellPostprocess(Context context) {
    for (CellPostprocessor cellPostprocessor : cellPostprocessors) {
      if (cellPostprocessor.support(context)) {
        cellPostprocessor.process(context);
      }
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    cellPreprocessors = applicationContext.getBeansOfType(CellPreprocessor.class).values();
    cellProcessors = applicationContext.getBeansOfType(CellProcessor.class).values();
    cellPostprocessors = applicationContext.getBeansOfType(CellPostprocessor.class).values();

  }

}
