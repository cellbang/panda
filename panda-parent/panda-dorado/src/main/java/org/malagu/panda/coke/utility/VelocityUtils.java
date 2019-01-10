package org.malagu.panda.coke.utility;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtils {
  private static VelocityEngine velocityEngine;

  static {
    velocityEngine = new VelocityEngine();
    Properties p = new Properties();
    p.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
    velocityEngine.init(p);

  }

  public static VelocityEngine getEngine() {
    return velocityEngine;
  }

  public static String parseString(String str, Map<String, Object> parameters) {
    if (parameters == null) {
      parameters = new HashMap<String, Object>();
    }
    VelocityContext context = new VelocityContext(parameters);
    StringWriter writer = new StringWriter();
    velocityEngine.evaluate(context, writer, "parseString", str.toString());
    return writer.toString();
  }

}
