package org.malagu.panda.coke.viewgenerator.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerUtil {

  public static String generate(String templateCode, Object dataModel) {
    Writer out = null;
    try {
      Template template = cfg.getTemplate(templateCode, "utf-8");
      out = new StringWriter(2048);
      template.process(dataModel, out);
    } catch (TemplateException e) {
      e.getBlamedExpressionString();
      throw new RuntimeException("变量不存在 " + e.getBlamedExpressionString(), e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return out != null ? out.toString() : null;
  }

  public void update(String templateCode, String content) {
    // TODO Auto-generated method stub

  }

  private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

  static {
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    ClassTemplateLoader ctl =
        new ClassTemplateLoader(FreeMarkerUtil.class, "/viewgenerator");
    cfg.setTemplateLoader(ctl);
  }

  public static void main(String[] args) {

    Map<String, Object> dataModel = new HashMap<>();
    dataModel.put("user", "Bing");

    String result = FreeMarkerUtil.generate("hello.ftl", dataModel);
    System.out.println(result);


  }
}
