package org.malagu.panda.coke.context;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.xobo.toolkit.DateUtil;
import org.xobo.toolkit.NumberParser;
import org.xobo.toolkit.PatternUtil;
import com.bstek.dorado.core.el.ContextVarsInitializer;

public class CokeContextVarsInitializer implements ContextVarsInitializer {

  @Override
  public void initializeContext(Map<String, Object> vars) throws Exception {
    vars.put("coke", cokeAction);
    vars.put("env", env);
  }

  public CokeContextVarsInitializer() {
    cokeAction = new HashMap<>();
    cokeAction.put("string", PatternUtil.class);
    cokeAction.put("date", DateUtil.class);
    cokeAction.put("number", NumberParser.class);
  }


  @Autowired
  private Environment env;
  private Map<String, Object> cokeAction;

}
