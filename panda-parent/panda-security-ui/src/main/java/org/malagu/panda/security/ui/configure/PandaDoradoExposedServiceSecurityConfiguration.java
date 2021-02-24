package org.malagu.panda.security.ui.configure;

import java.util.Collection;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xobo.dorado.exposedservice.security.configuration.DoradoExposedServiceSecurityConfiguration;
import org.xobo.dorado.exposedservice.security.service.UrlRedirectFilter;
import org.xobo.dorado.exposedservice.security.service.UrlRedirectMappingProvider;

@Configuration
@Import(DoradoExposedServiceSecurityConfiguration.class)
public class PandaDoradoExposedServiceSecurityConfiguration {

  @Bean
  public FilterRegistrationBean<Filter> someFilterRegistration(
      Collection<UrlRedirectMappingProvider> urlRedirectMappingProviders) {

    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
    registration.setFilter(urlRedirectFilter(urlRedirectMappingProviders));
    registration.addUrlPatterns("/*");
    registration.setName("urlRedirectFilter");
    registration.setOrder(1);
    return registration;
  }

  @Bean
  public Filter urlRedirectFilter(
      Collection<UrlRedirectMappingProvider> urlRedirectMappingProviders) {
    return new UrlRedirectFilter(urlRedirectMappingProviders);
  }

}
