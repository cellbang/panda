package org.malagu.panda.autoconfigure.profile;

import org.malagu.panda.autoconfigure.security.SecurityAutoConfiguration;
import org.malagu.panda.profile.ProfileConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年7月18日
 */
@Configuration
@ConditionalOnClass(ProfileConfiguration.class)
@AutoConfigureBefore({JpaRepositoriesAutoConfiguration.class})
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(ProfileConfiguration.class)
public class ProfileAutoConfiguration {
	
}
