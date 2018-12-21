package org.malagu.panda.coke.context;

import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.PackageConfigurer;

/**
 * @author Jacky.gao
 * @since 2013年5月21日
 */
public class CokeContextLocationConfigurer implements PackageConfigurer {

  @Override
  public String[] getPropertiesConfigLocations(ResourceLoader resourceLoader) throws Exception {
    return new String[] {"classpath:coke/coke.config.properties"};
  }

  @Override
  public String[] getContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
    String contextCore = "classpath:coke/coke.context.xml";
    return new String[] {contextCore};
  }

  @Override
  public String[] getServletContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
    return null;
  }
}
