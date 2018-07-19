package org.malagu.panda.log;

import java.util.Collection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.malagu.panda.log.annotation.Log;
import org.malagu.panda.log.annotation.LogDefinition;
import org.malagu.panda.log.context.ContextHandler;
import org.malagu.panda.log.context.provider.ContextProvider;
import org.malagu.panda.log.logger.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *@author Kevin.yang
 *@since 2015年7月19日
 */
@Component
@Aspect
public class LogAspect implements ApplicationContextAware {
	
	@Autowired
	protected ContextHandler contextHandler;
	
	@Autowired
	protected Collection<ContextProvider> providers;
	
	protected ApplicationContext applicationContext;
	
	@Value("${panda.log.disabled}")
	private boolean disabled;
	
	@Pointcut("@annotation(org.malagu.panda.log.annotation.NotLog)")
	public void notLog(){}
	
	@Pointcut("within(org.malagu.panda.log.proxy.LogProxy)")
	public void logProxy(){}
	
	@Pointcut(value = "@within(org.malagu.panda.log.annotation.Log)")
	public void logType(){}
	
	@Pointcut(value = "@annotation(org.malagu.panda.log.annotation.Log)")
	public void logMethod(){}
	
	@Pointcut(value = "logType() && logMethod() && !logProxy()")
	public void logTypeAndMethod(){}
	
	@Pointcut(value = "logType() && !logMethod() && !notLog() && !logProxy()")
	public void logOnlyType(){}
	
	@Pointcut(value = "!logType() && logMethod() && !logProxy()")
	public void logOnlyMethod(){}
	
	@Around(value = "logOnlyMethod() && @annotation(log)", argNames = "log")
	public Object logOnlyMethodAround(ProceedingJoinPoint joinPoint, Log log) throws Throwable{
		LogDefinition logDefinition =LogUtils.buildLogDefinition(log);
		return doLogAround(logDefinition, joinPoint);
	}
	
	@Around(value = "logMethod() && @target(log)", argNames = "log")
	public Object logOnlyTypeAround(ProceedingJoinPoint joinPoint, Log log) throws Throwable{
		LogDefinition logDefinition =LogUtils.buildLogDefinition(log);
		return doLogAround(logDefinition, joinPoint);
	}
	
	@Around(value = "logMethod() && @target(typeLog) && @annotation(methodLog)", argNames = "typeLog,methodLog")
	public Object logTypeMethodAround(ProceedingJoinPoint joinPoint, Log typeLog, Log methodLog) throws Throwable{
		LogDefinition logDefinition =LogUtils.buildLogDefinition(typeLog, methodLog);
		return doLogAround(logDefinition, joinPoint);
	}
	
	private Object doLogAround(LogDefinition logDefinition,
			ProceedingJoinPoint joinPoint) throws Throwable {
		Object returnValue = joinPoint.proceed();
		if (!disabled) {
			contextHandler.set(ContextProvider.LOG_DEFINITION, logDefinition);
			contextHandler.set(ContextProvider.JOIN_POINT, joinPoint);
			contextHandler.set(ContextProvider.RETURN_VALUE, returnValue);
			contextHandler.set(ContextProvider.ARGS, joinPoint.getArgs());
			Logger logger = (Logger) applicationContext.getBean(logDefinition.getLogger());
			for (ContextProvider provider : providers) {
				if (provider.support(logDefinition)) {
					contextHandler.set(ContextProvider.TARGET, provider.getContext());
					logger.log();
					break;
				}
			}
		}
		return returnValue;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

}
