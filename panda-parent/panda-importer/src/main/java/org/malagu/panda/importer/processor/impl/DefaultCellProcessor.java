package org.malagu.panda.importer.processor.impl;


import java.util.Collection;
import java.util.Objects;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.malagu.panda.importer.Constants;
import org.malagu.panda.importer.converter.TypeConverter;
import org.malagu.panda.importer.exception.DataFormatException;
import org.malagu.panda.importer.model.MappingRule;
import org.malagu.panda.importer.parser.CellPostParser;
import org.malagu.panda.importer.parser.CellPreParser;
import org.malagu.panda.importer.policy.Context;
import org.malagu.panda.importer.processor.CellProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Kevin.yang
 * @since 2015年8月30日
 */
public class DefaultCellProcessor implements CellProcessor, ApplicationContextAware {

  private Collection<TypeConverter> typeConverters;

  private ApplicationContext applicationContext;


  private Log log = LogFactory.getLog(DefaultCellProcessor.class);

  @Override
  public void process(Context context) {
    cellPreParse(context);
    cellParse(context);
    cellPostParse(context);
  }

  protected void cellPreParse(Context context) {
    MappingRule mappingRule = context.getCurrentMappingRule();
    String cellPreParserBean = mappingRule.getCellPreParserBean();
    CellPreParser cellPreParser = (CellPreParser) applicationContext.getBean(cellPreParserBean);
    cellPreParser.parse(context);
  }

  protected void cellParse(Context context) {
    MappingRule mappingRule = context.getCurrentMappingRule();
    BeanMap beanMap = BeanMap.create(context.getCurrentEntity());
    String propertyName = mappingRule.getPropertyName();
    Class<?> type = beanMap.getPropertyType(propertyName);
    if (type == null) {
      return;
    }
    Object value = context.getValue();
    for (TypeConverter typeConverter : typeConverters) {
      if (typeConverter.support(type)) {
        try {
          value = typeConverter.fromObject(type, mappingRule.getMappingValueIfNeed(value));
        } catch (Exception e) {
          if (mappingRule.isIgnoreErrorFormatData()) {
            log.debug(e.getMessage());
            value = Constants.IGNORE_ERROR_FORMAT_DATA;
          } else {
            throw new DataFormatException(context.getCurrentCell().getRow(),
                context.getCurrentCell().getCol(), Objects.toString(value, null));
          }
        }
        break;
      }
    }
    context.setValue(value);
  }

  protected void cellPostParse(Context context) {
    MappingRule mappingRule = context.getCurrentMappingRule();
    String cellPostParserBean = mappingRule.getCellPostParserBean();
    CellPostParser cellPostParser = (CellPostParser) applicationContext.getBean(cellPostParserBean);
    cellPostParser.parse(context);
  }

  @Override
  public boolean support(Context context) {
    return true;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    this.applicationContext = applicationContext;
    typeConverters = applicationContext.getBeansOfType(TypeConverter.class).values();
  }

}
