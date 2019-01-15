package org.malagu.panda.coke.datasource.service.impl;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 动态数据源注册
 * @author sr on 2019-01-10 
 */
@Configuration
@EnableTransactionManagement
public class DynamicDataSourceRegister {
  
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  @Primary
  public DataSourceProperties primaryDataSourceProperties() {
    return new DataSourceProperties(); 
  }
  
  @Bean(name = "defaultDataSource")
  public DataSource defaultDataSource() {
    return primaryDataSourceProperties().initializeDataSourceBuilder().build();
  }
  
  @Bean(name = "multiDataSource")
  @Primary
  public DataSource multiDataSource() {
    return new MultiDataSource();
  }
  
}
