package org.malagu.panda.coke.datasource.dao;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.MapUtils;
import org.malagu.panda.coke.datasource.entity.CokeDataSourceInfo;
import org.malagu.panda.coke.utility.Coke;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

/**
 * 数据源配置表dao
 * @author sr on 2019-01-02 
 */
@Repository
public class DataSourceInfoDao {
  
  @Transactional
  public List<CokeDataSourceInfo> getDataSourceInfos(Map<String, Object> params) {
    StringBuilder hql = new StringBuilder();
    Map<String, Object> hqlParams = Maps.newHashMap();
    hql.append(" from CokeDataSourceInfo where deleted = 0 ");
    Long name = MapUtils.getLong(params, "name");
    if (name != null) {
      hql.append(" and name =:name ");
      hqlParams.put("name", name);
    }
    return Coke.query(hql.toString(), hqlParams);
  }

}
