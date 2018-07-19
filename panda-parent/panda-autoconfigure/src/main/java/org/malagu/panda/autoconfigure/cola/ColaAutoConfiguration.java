package org.malagu.panda.autoconfigure.cola;

import org.malagu.panda.cola.configure.ColaConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@Configuration
@ConditionalOnClass(ColaConfiguration.class)
@Import(ColaConfiguration.class)
public class ColaAutoConfiguration {
	
}
