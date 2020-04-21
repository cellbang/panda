package org.malagu.panda.coke.datamask.view;

import java.util.Collection;
import java.util.Map;
import ${entity.clazz};
import org.malagu.panda.coke.utility.Coke;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service
@Transactional(readOnly = true)
public class ${entity.name}Maintain {

  @DataProvider
  public void load${entity.name}(Page<${entity.name}> page, Criteria criteria, Map<String, Object> parameterMap) {
    Coke.query(${entity.name}.class, criteria, parameterMap).paging(page);
  }
  
  <#list entity.children as child>
  @DataProvider
  public void load${child.name}(Page<${child.name}> page, Criteria criteria, Map<String, Object> parameterMap) {
    Coke.query(${child.name}.class, criteria, parameterMap).paging(page);
  }  
  </#list>

  @DataResolver
  @Transactional
  public void save${entity.name}(Collection<${entity.name}> ${entity.name?uncap_first}List) {
    Coke.save(${entity.name?uncap_first}List);
  }
}
