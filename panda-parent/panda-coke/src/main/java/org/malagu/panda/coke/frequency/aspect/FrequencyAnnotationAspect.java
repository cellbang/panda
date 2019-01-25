package org.malagu.panda.coke.frequency.aspect;

import java.util.Calendar;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.malagu.panda.coke.frequency.Frequency;
import org.malagu.panda.coke.frequency.constant.FrequencyConstants;
import org.malagu.panda.coke.frequency.constant.FrequencyConstants.Unit;
import org.malagu.panda.coke.frequency.model.BusinessInvokeSetting;
import org.malagu.panda.coke.frequency.service.BusinessInvokeService;
import org.malagu.panda.coke.frequency.service.BusinessInvokeSettingCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Frequency注解切面
 * @author sr on 2019-01-24 
 */
@Component
@Aspect
public class FrequencyAnnotationAspect {
  
  @Autowired
  private BusinessInvokeService businessInvokeService;
  
  @Autowired
  private BusinessInvokeSettingCacheService businessInvokeSettingCacheService;
  
  @Around(value = "@annotation(frequency)")
  public Object invokeMethod(ProceedingJoinPoint point, Frequency frequency) throws Throwable {
    String code = frequency.value();
    BusinessInvokeSetting setting =  businessInvokeSettingCacheService.getBusinessInvokeSetting(code);
    if (setting == null) {
      return point.proceed();
    }
    Unit unit = Unit.getUnit(setting.getUnit());
    String key = FrequencyConstants.METHOD_INVOKE_COUNT_CACHE_NAME + ":" + unit + "_" + getDate(unit) + "_" + code;
    Long currentNum = businessInvokeService.getCurrentNum(key, unit);
    if (currentNum > setting.getNum()) {
      businessInvokeService.rollbackNum(key);
      throw new RuntimeException("调用超过上限");
    }
    return point.proceed();
  }
  
  private int getDate(Unit unit) {
    Calendar c = Calendar.getInstance();
    if (unit == Unit.YEAR) {
      return c.get(Calendar.YEAR);
    } else if (unit == Unit.MONTH) {
      return c.get(Calendar.MONTH);
    } else if (unit == Unit.WEEKEND){
      return c.get(Calendar.WEEK_OF_YEAR);
    } else {
      return c.get(Calendar.DAY_OF_YEAR);
    }
  }
  

}
