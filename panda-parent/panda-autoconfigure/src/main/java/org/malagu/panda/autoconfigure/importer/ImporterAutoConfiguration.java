package org.malagu.panda.autoconfigure.importer;

import org.malagu.panda.autoconfigure.security.SecurityAutoConfiguration;
import org.malagu.panda.importer.ImporterConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月24日
 */
@Configuration
@ConditionalOnClass(ImporterConfiguration.class)
@AutoConfigureBefore({JpaRepositoriesAutoConfiguration.class})
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(ImporterConfiguration.class)
public class ImporterAutoConfiguration {
	
}
