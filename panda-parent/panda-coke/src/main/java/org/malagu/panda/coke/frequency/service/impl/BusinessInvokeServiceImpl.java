package org.malagu.panda.coke.frequency.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.malagu.panda.coke.frequency.constant.FrequencyConstants.Unit;
import org.malagu.panda.coke.frequency.service.BusinessInvokeService;
import org.malagu.panda.coke.frequency.util.FrequencyUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 方法调用控制service实现类
 * @author sr on 2019-01-24
 */
@Service
public class BusinessInvokeServiceImpl implements BusinessInvokeService {

  @Resource
  private RedisTemplate<String, Integer> redisTemplate;

  @Override
  public Long getCurrentNum(String key, Unit unit) {
    Boolean hasKey = redisTemplate.hasKey(key);
    Long currentNum = redisTemplate.opsForValue().increment(key, 1);
    if (!hasKey) {
      redisTemplate.expire(key, FrequencyUtils.getExpireDays(unit), TimeUnit.DAYS);
    }
    return currentNum;
  }

  @Override
  public void rollbackNum(String key) {
    redisTemplate.opsForValue().increment(key, -1);
  }

}
