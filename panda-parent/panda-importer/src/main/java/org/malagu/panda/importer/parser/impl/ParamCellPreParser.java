package org.malagu.panda.importer.parser.impl;

import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.importer.model.MappingRule;
import org.malagu.panda.importer.parser.CellPreParser;
import org.malagu.panda.importer.policy.Context;

public class ParamCellPreParser implements CellPreParser {

  @Override
  public String getName() {
    return "参数解析";
  }

  @Override
  public void parse(Context context) {
    context.setValue(context.getCurrentCell().getValue());
    MappingRule mappingRule = context.getCurrentMappingRule();
    String cellPreParserParam = mappingRule.getCellPreParserParam();
    if (StringUtils.isEmpty(cellPreParserParam)) {
      throw new RuntimeException("'" + getName() + "'参数不能为空！");
    }
    Object value = context.getParams().get(cellPreParserParam);
    context.setValue(value);
  }

}
