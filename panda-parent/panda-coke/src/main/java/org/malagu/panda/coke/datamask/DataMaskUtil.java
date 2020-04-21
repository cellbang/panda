package org.malagu.panda.coke.datamask;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.malagu.panda.coke.datamask.domain.DataMaskRule;

public class DataMaskUtil {
  public static String mask(DataMaskRule dataMaskRule, String data) {
    return data.replaceAll(dataMaskRule.getRegex(), dataMaskRule.getReplacement());
  }

  public static String buildReplacement(DataMaskRule dataMaskRule) {
    String hiddenGroup = dataMaskRule.getHiddenGroup();
    String regex = dataMaskRule.getRegex();
    String mask = StringUtils.defaultString(dataMaskRule.getMask(), "***");

    StringBuilder builder = new StringBuilder();
    List<Integer> hiddenGroupList = getHiddenGroup(hiddenGroup);
    int groupCount = StringUtils.countMatches(regex, '(');
    for (int i = 1; i < groupCount + 1; i++) {
      if (hiddenGroupList.contains(i)) {
        builder.append(mask);
      } else {
        builder.append("$").append(i);
      }
    }
    String replacement = builder.toString();
    dataMaskRule.setReplacement(replacement);
    return replacement;
  }

  private static List<Integer> getHiddenGroup(String hiddenGroup) {
    String[] hiddenGroupStrs = hiddenGroup.split(",");
    List<Integer> groupIds = new ArrayList<Integer>();

    for (String string : hiddenGroupStrs) {
      groupIds.add(Integer.parseInt(string));
    }

    return groupIds;
  }

}
