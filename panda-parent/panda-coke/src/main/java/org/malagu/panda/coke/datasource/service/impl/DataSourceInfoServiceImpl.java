package org.malagu.panda.coke.datasource.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.datasource.entity.CokeDataSourceInfo;
import org.malagu.panda.coke.datasource.service.DataSourceInfoService;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据源配置信息service实现类
 * @author sr on 2019-01-02 
 */
@Service(DataSourceInfoService.BEAN_ID)
public class DataSourceInfoServiceImpl implements DataSourceInfoService {

  @Resource(name = "defaultDataSource")
  private DataSource defaultDataSource;

  private Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();


  private Map<String, CokeDataSourceInfo> dataSourceInfoMap = Maps.newConcurrentMap();

  public DataSource createDataSource(String name) {
    CokeDataSourceInfo dataSourceInfo = dataSourceInfoMap.get(name);
    if (dataSourceInfo == null) {
      return defaultDataSource;
    }
    String url = dataSourceInfo.getUrl();
    String username = dataSourceInfo.getUsername();
    String password = dataSourceInfo.getPassword();
    String driverClassName = dataSourceInfo.getDriverClassName();
    if (defaultDataSource instanceof DruidDataSource) {
      DruidDataSource dataSource = ((DruidDataSource) defaultDataSource).cloneDruidDataSource();
      dataSource.setName(name);
      dataSource.setUrl(url);
      dataSource.setUsername(username);
      dataSource.setPassword(password);
      dataSource.setDriverClassName(driverClassName);
      return dataSource;
    } else if (defaultDataSource instanceof BasicDataSource) {
      BasicDataSource defaultBasicDataSource = (BasicDataSource)defaultDataSource;
      BasicDataSource dataSource = new BasicDataSource();
      dataSource.setDriverClassName(driverClassName);
      dataSource.setUrl(url);
      dataSource.setUsername(username);
      dataSource.setPassword(password);
      dataSource.setMinIdle(defaultBasicDataSource.getMinIdle());
      dataSource.setMaxActive(defaultBasicDataSource.getMaxActive());
      dataSource.setMinEvictableIdleTimeMillis(defaultBasicDataSource.getMinEvictableIdleTimeMillis());
      dataSource.setTimeBetweenEvictionRunsMillis(defaultBasicDataSource.getTimeBetweenEvictionRunsMillis());
      dataSource.setValidationQuery(defaultBasicDataSource.getValidationQuery());
      dataSource.setValidationQueryTimeout(defaultBasicDataSource.getValidationQueryTimeout());
      return dataSource;
    } else if (defaultDataSource instanceof HikariDataSource) {
      HikariDataSource defaultHikariDataSource = (HikariDataSource)defaultDataSource;
      HikariConfig newHikariConfig = new HikariConfig();
      defaultHikariDataSource.copyStateTo(newHikariConfig);
      newHikariConfig.setDriverClassName(driverClassName);
      newHikariConfig.setJdbcUrl(url);
      newHikariConfig.setUsername(username);
      newHikariConfig.setPassword(password);
      return new HikariDataSource(newHikariConfig);
    }
    return defaultDataSource;
  }

  @Override
  public DataSource getDataSource(String name) {
    if (StringUtils.isBlank(name)) {
      return defaultDataSource;
    }

    DataSource dataSource = dataSourceMap.get(name);
    if (dataSource == null) {
      dataSource = createDataSource(name);
      dataSourceMap.put(name, dataSource);
    }
    return dataSource;
  }

  public void clearDataSource() {
    dataSourceMap.clear();
  }

  @Override
  public void initDataSourceInfoMap(List<CokeDataSourceInfo> dataSourceInfos) {
    dataSourceInfoMap = dataSourceInfos.stream().collect(
        Collectors.toMap(CokeDataSourceInfo::getName, c -> c));
  }

  @Override
  public Set<String> getDataSourceNames() {
    return dataSourceInfoMap.keySet();
  }

}
