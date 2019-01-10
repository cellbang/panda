package org.malagu.panda.security.ui.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import com.bstek.dorado.common.proxy.PatternMethodInterceptor;

public class AjaxServiceInterceptor extends PatternMethodInterceptor {
	
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
       
    	return methodInvocation.proceed();
    }
}