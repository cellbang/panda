package org.malagu.panda.coke.datasource.listener;

import java.util.List;

import javax.annotation.Resource;

import org.malagu.panda.coke.datasource.dao.DataSourceInfoDao;
import org.malagu.panda.coke.datasource.entity.CokeDataSourceInfo;
import org.malagu.panda.coke.datasource.service.DataSourceInfoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.bstek.dorado.core.EngineStartupListener;

/**
 * 初始化dataSourceInfoMap
 * 
 * @author sr on 2019-01-09
 */
@Service
@ConditionalOnProperty(prefix = "coke", name = "dynamic.datasource")
public class DataSourceStartupListener extends EngineStartupListener {

  @Resource
  private DataSourceInfoDao dataSourceInfoDao;

  @Resource(name = DataSourceInfoService.BEAN_ID)
  private DataSourceInfoService dataSourceInfoService;

  @Override
  public void onStartup() throws Exception {
    List<CokeDataSourceInfo> dataSourceInfos = dataSourceInfoDao.getDataSourceInfos(null);
    dataSourceInfoService.initDataSourceInfoMap(dataSourceInfos);
  }

}
