package org.malagu.panda.coke.datasource.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.malagu.panda.coke.datasource.service.DataSourceInfoService;
import org.malagu.panda.security.ContextUtils;
import org.springframework.jdbc.datasource.AbstractDataSource;

public class MultiDataSource extends AbstractDataSource{
  
  @Resource(name = DataSourceInfoService.BEAN_ID)
  private DataSourceInfoService dataSourceInfoService;

  @Resource(name = "defaultDataSource")
  private DataSource defaultTargetDataSource;

  protected DataSource determineTargetDataSource() {
    // TODO 获取需要替换的名称
    String name = ContextUtils.getLoginUsername();
    return dataSourceInfoService.getDataSource(name);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return determineTargetDataSource().getConnection();
  }


  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return determineTargetDataSource().getConnection(username, password);
  }


  public DataSourceInfoService getDataSourceInfoService() {
    return dataSourceInfoService;
  }

  public void setDataSourceInfoService(DataSourceInfoService dataSourceInfoService) {
    this.dataSourceInfoService = dataSourceInfoService;
  }

  public DataSource getDefaultTargetDataSource() {
    return defaultTargetDataSource;
  }

  public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
    this.defaultTargetDataSource = defaultTargetDataSource;
  }

}
