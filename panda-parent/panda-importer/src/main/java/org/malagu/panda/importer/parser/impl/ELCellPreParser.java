package org.malagu.panda.importer.parser.impl;

import java.util.UUID;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.importer.model.MappingRule;
import org.malagu.panda.importer.parser.CellPreParser;
import org.malagu.panda.importer.policy.Context;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.el.ExpressionHandler;

/**
 * @author Kevin.yang
 * @since 2015年8月30日
 */
public class ELCellPreParser implements CellPreParser, ApplicationContextAware {

  private ExpressionHandler expressionHandler;

  @Override
  public String getName() {
    return "EL表达式前置解析器";
  }

  @Override
  public void parse(Context context) {
    MappingRule mappingRule = context.getCurrentMappingRule();
    Object value = context.getCurrentCell().getValue();
    String cellPreParserParam = mappingRule.getCellPreParserParam();
    JexlContext jexlContext = expressionHandler.getJexlContext();
    jexlContext.set("context", context);
    jexlContext.set("value", value);
    jexlContext.set("UUID", UUID.randomUUID().toString());
    injectBeans(mappingRule.getInjects(), jexlContext);
    Expression expression = expressionHandler.compile(cellPreParserParam);
    if (expression == null) {
      value = cellPreParserParam;
    } else {
      value = expression.evaluate();
    }
    context.setValue(value);;
  }

  public void injectBeans(String injects, JexlContext jexlContext) {
    if (StringUtils.isEmpty(injects)) {
      return;
    }
    String[] beanIds = injects.split(",");
    for (String beanId : beanIds) {
      if (StringUtils.isEmpty(beanId)) {
        continue;
      }

      Object bean = null;
      try {
        bean = applicationContext.getBean(beanId);
      } catch (BeansException e) {

      }

      if (bean != null) {
        jexlContext.set(beanId, bean);
      }
    }

  }

  public void setExpressionHandler(ExpressionHandler expressionHandler) {
    this.expressionHandler = expressionHandler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.
   * context.ApplicationContext)
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  private ApplicationContext applicationContext;
}
