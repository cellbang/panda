package org.malagu.panda.importer.parser.impl;

import org.malagu.panda.importer.parser.CellPreParser;
import org.malagu.panda.importer.policy.Context;

public class FixValueCellPreParser implements CellPreParser {

  @Override
  public String getName() {
    return "固定值";
  }

  @Override
  public void parse(Context context) {
    context.setValue(context.getCurrentMappingRule().getCellPreParserParam());
  }

}
