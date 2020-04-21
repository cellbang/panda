package org.malagu.panda.coke.viewgenerator.view;

import java.util.Collection;
import java.util.Map;
import org.malagu.panda.coke.utility.Coke;
import org.malagu.panda.coke.viewgenerator.domain.EntityDef;
import org.malagu.panda.coke.viewgenerator.domain.Property;
import org.malagu.panda.coke.viewgenerator.util.ViewGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service
@Transactional(readOnly = true)
public class ViewGeneratorMaintain {

  @DataProvider
  public void loadEntityDef(Page<EntityDef> page, Criteria criteria,
      Map<String, Object> parameterMap) {
    Coke.query(EntityDef.class, criteria, parameterMap).paging(page);
  }
  

  @DataProvider
  public void loadProperty(Page<Property> page, Criteria criteria,
      Map<String, Object> parameterMap) {
    Coke.query(Property.class, criteria, parameterMap).paging(page);
  }

  @DataResolver
  @Transactional
  public void saveEntityDef(Collection<EntityDef> entityDefList) {
    Coke.save(entityDefList);
  }

  @DataProvider
  public Collection<EntityDef> importEntityDef(String packagePath) {
    return ViewGeneratorUtil.importEntityDef(packagePath);
  }
}
