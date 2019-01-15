package org.malagu.panda.coke.datasource.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.malagu.panda.coke.datasource.service.DataSourceInfoService;
import org.springframework.jdbc.datasource.AbstractDataSource;

/**
 * 多数据源
 * @author sr on 2019-01-10 
 */
public class MultiDataSource extends AbstractDataSource{
  
  @Resource(name = DataSourceInfoService.BEAN_ID)
  private DataSourceInfoService dataSourceInfoService;

  @Resource(name = "defaultDataSource")
  private DataSource defaultTargetDataSource;
  
  private static final ThreadLocal<String> CURRENT_HOLDER = new ThreadLocal<String>();
  
  public static void setCurrentDataSourceName(String dataSourceName) {
    CURRENT_HOLDER.set(dataSourceName);
  }
  
  public static String getCurrentDataSourceName() {
    return CURRENT_HOLDER.get();
  }

  protected DataSource determineTargetDataSource() {
    String name = getCurrentDataSourceName();
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
