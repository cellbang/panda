package org.malagu.panda.coke.datamask.view;

import java.util.Collection;
import java.util.Map;
import org.malagu.panda.coke.datamask.domain.DataMaskRule;
import org.malagu.panda.coke.utility.Coke;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service
@Transactional(readOnly = true)
public class DataMaskRuleMaintain {

  @DataProvider
  public void loadDataMaskRule(Page<DataMaskRule> page, Criteria criteria,
      Map<String, Object> parameterMap) {
    Coke.query(DataMaskRule.class, criteria, parameterMap).paging(page);
  }

  @DataResolver
  @Transactional
  public void saveDataMaskRule(Collection<DataMaskRule> dataMaskRuleList) {
    Coke.save(dataMaskRuleList);
  }
}
