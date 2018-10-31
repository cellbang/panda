package org.malagu.panda.coke.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;


public class NumberParser {
  public static List<Long> toLongs(String nums) {
    if (StringUtils.isEmpty(nums)) {
      return Collections.emptyList();
    }
    String[] numTokens = nums.split(",");
    List<Long> numbers = new ArrayList<Long>(numTokens.length);
    for (String token : numTokens) {
      numbers.add(Long.valueOf(token));
    }
    return numbers;
  }

  public static Long parseLong(Object obj) {
    if (obj == null) {
      return null;
    } else if (obj instanceof Number) {
      return ((Number) obj).longValue();
    } else {
      Long val = null;
      try {
        val = Long.parseLong(obj.toString());
      } catch (Exception e) {

      }
      return val;
    }
  }

  public static Long parseLong(Object obj, Long defaultValue) {
    if (obj == null) {
      return defaultValue;
    } else if (obj instanceof Number) {
      return ((Number) obj).longValue();
    } else {
      Long val = null;
      try {
        val = Long.parseLong(obj.toString());
      } catch (Exception e) {
        val = defaultValue;
      }
      return val;
    }

  }

  public static Integer parseInteger(Object obj) {
    if (obj == null) {
      return null;
    } else if (obj instanceof Number) {
      return ((Number) obj).intValue();
    } else {
      Integer val = null;
      try {
        val = Integer.parseInt(obj.toString());
      } catch (Exception e) {

      }
      return val;
    }
  }

  public static BigDecimal parseDecimal(Object obj) {
    if (obj == null || obj instanceof BigDecimal) {
      return (BigDecimal) obj;
    }

    String value = obj.toString();
    value = StringUtil.find(value, PATTERN_FLOAT, 1);
    if (StringUtils.isNotEmpty(value)) {
      value = value.replaceAll(",", "");
    } else {
      return null;
    }
    BigDecimal bigDecimal = new BigDecimal(value);
    return bigDecimal;
  }

  public static List<Integer> parseIntegerList(String str) {
    return parseIntegerList(str, ",");
  }

  public static List<Integer> parseIntegerList(String str, String regex) {
    List<Integer> list = new ArrayList<Integer>();
    if (StringUtils.isNotEmpty(str)) {
      String[] tokens = str.split(regex);
      for (String token : tokens) {
        Integer val = parseInteger(token);
        if (val != null) {
          list.add(val);
        }
      }
    }
    return list;
  }

  private static Pattern PATTERN_FLOAT = Pattern.compile("([+-]?\\d[\\d,]*\\.\\d+)");
}
