package org.malagu.panda.coke.datasource.service;

import java.util.List;

import javax.sql.DataSource;

import org.malagu.panda.coke.datasource.entity.CokeDataSourceInfo;

/**
 * 数据源配置service
 * @author sr on 2019-01-02 
 */
public interface DataSourceInfoService {
  
  String BEAN_ID = "ck.dataSourceInfoService";
  
  /**
   * 初始化
   * @param dataSourceInfos
   * @author sr on 2019-01-09 
   */
  void initDataSourceInfoMap(List<CokeDataSourceInfo> dataSourceInfos);
  
  /**
   * 获取对应名称的数据源
   * @param name
   * @return
   * @author sr on 2019-01-02 
   */
  DataSource getDataSource(String name);
  
  

}
