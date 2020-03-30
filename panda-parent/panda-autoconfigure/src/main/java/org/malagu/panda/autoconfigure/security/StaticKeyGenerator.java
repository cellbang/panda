package org.malagu.panda.autoconfigure.security;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年8月14日
 */
public class StaticKeyGenerator implements KeyGenerator {
  
  @Value("${panda.cachePrefix:panda}")
  private String cachePrefix;
	
	@Override
	public Object generate(Object target, Method method, Object... params) {
		return cachePrefix;
	}

}
