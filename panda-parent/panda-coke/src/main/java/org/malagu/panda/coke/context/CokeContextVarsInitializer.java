package org.malagu.panda.coke.context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.xobo.toolkit.DateUtil;
import org.xobo.toolkit.NumberParser;
import org.xobo.toolkit.PatternUtil;
import com.bstek.dorado.core.el.ContextVarsInitializer;

@Service
public class CokeContextVarsInitializer implements ContextVarsInitializer {

  @Override
  public void initializeContext(Map<String, Object> vars) throws Exception {
    vars.put("coke", cokeAction);
  }

  public CokeContextVarsInitializer() {
    cokeAction = new HashMap<>();
    cokeAction.put("string", PatternUtil.class);
    cokeAction.put("date", DateUtil.class);
    cokeAction.put("number", NumberParser.class);
  }


  public static void main(String[] args) {
    String str = "2018-09-04-20.51.52.103794";
    Date date = DateUtil.parse(str, "yyyy-MM-dd-HH.mm.ss");
    System.out.println(date);
    System.out.println(NumberParser.parseDecimal("伍万元整,￥50,000.00元"));
  }

  private Map<String, Object> cokeAction;

}
