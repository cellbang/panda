package org.malagu.panda.coke.base.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.base.domain.CokeArea;
import org.malagu.panda.coke.utility.Coke;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Transactional(readOnly = true)
@Service
public class CokeAreaMaintain {
  @DataProvider
  public List<CokeArea> loadCokeArea(Map<String, Object> parameterMap) {
    return Coke.query(CokeArea.class, null, parameterMap).list();
  }

  @DataProvider
  public void loadCokeAreaPaging(Page<CokeArea> page, Criteria criteria, Map<String, Object> parameterMap) {
    Coke.query(CokeArea.class, criteria, parameterMap).paging(page);
  }

  @DataProvider
  public void loadCokeAreaPaging(Page<CokeArea> page, Criteria criteria, Map<String, Object> parameter,
      String filterValue) {
    if (StringUtils.isNotBlank(filterValue)) {
      filterValue = filterValue.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
    }
    Coke.query(CokeArea.class, criteria, parameter)
      .addIf(filterValue)
        .or()
        .like("name", filterValue + "%")
        .like("initials", filterValue + "%")
        .like("pinyin", filterValue + "%")
        .end()
      .endIf()
      .paging(page);
  }

  @DataProvider
  @Cacheable("coke.area")
  public List<CokeArea> loadCokeAreaTree() {
    List<CokeArea> cokeAreaList = Coke.query(CokeArea.class, null, null).list();
    List<CokeArea> topCokeAreaList = new ArrayList<CokeArea>();

    Map<Long, CokeArea> cokeAreaMap = cokeAreaList.stream().collect(Collectors.toMap(CokeArea::getId, x -> x));
    for (CokeArea cokeArea : cokeAreaList) {
      Long pid = cokeArea.getPid();

      if (pid == 0L) {
        topCokeAreaList.add(cokeArea);
      } else {
        CokeArea pcokeArea = cokeAreaMap.get(pid);
        if (pcokeArea != null) {
          pcokeArea.addChild(cokeArea);
        }
      }
    }

    return topCokeAreaList;
  }
}
