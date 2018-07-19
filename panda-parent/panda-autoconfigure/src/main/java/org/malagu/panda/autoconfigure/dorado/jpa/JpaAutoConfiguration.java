package org.malagu.panda.autoconfigure.dorado.jpa;

import org.malagu.panda.autoconfigure.dorado.DoradoAutoConfiguration;
import org.malagu.panda.dorado.linq.configure.JpaConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@Configuration
@ConditionalOnClass(JpaConfiguration.class)
@AutoConfigureAfter(DoradoAutoConfiguration.class)
@Import(JpaConfiguration.class)
public class JpaAutoConfiguration {
	
	
}
