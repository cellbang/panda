package org.malagu.panda.coke.datamask.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.dataType.MapMap;
import org.malagu.panda.coke.datamask.DataMaskUtil;
import org.malagu.panda.coke.datamask.domain.DataMaskRule;
import org.malagu.panda.coke.datamask.service.DataMaskService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import org.xobo.toolkit.BeanReflectionUtil;

@Service
public class DataMaskServiceImpl implements DataMaskService {

  public static void main(String[] args) {
    DataMaskRule dataMaskRule = new DataMaskRule();
    dataMaskRule.setRegex("(.{3})(.*)(.{4})");
    dataMaskRule.setHiddenGroup("2");
    DataMaskUtil.buildReplacement(dataMaskRule);
    System.out.println(DataMaskUtil.mask(dataMaskRule, "18616314917"));

    dataMaskRule = new DataMaskRule();
    dataMaskRule.setRegex("(.)(.*)");
    dataMaskRule.setHiddenGroup("2");
    DataMaskUtil.buildReplacement(dataMaskRule);
    System.out.println(DataMaskUtil.mask(dataMaskRule, "周兵兵"));
  }

  @Override
  public Object mask(Object entity) {
    Class<?> clazz = BeanReflectionUtil.getClass(entity);

    Map<String, DataMaskRule> props = DATA_MASK_MAP.get(clazz.getCanonicalName());
    for (Entry<String, DataMaskRule> entry : props.entrySet()) {
      String key = entry.getKey();
      DataMaskRule dataMaskRule = entry.getValue();

      if (dataMaskRule == null) {
        continue;
      }

      BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(dataMaskRule);
      String propertyValue = Objects.toString(beanWrapper.getPropertyValue(key), null);
      if (StringUtils.isEmpty(propertyValue)) {
        continue;
      }

      dataMaskRule.getRegex();

    }

    return null;
  }



  public DataMaskRule getDataMaskRule(Long dataMaskRuleId) {
    return null;
  }


  private static MapMap<String, String, DataMaskRule> DATA_MASK_MAP = MapMap.hashMap();

}
