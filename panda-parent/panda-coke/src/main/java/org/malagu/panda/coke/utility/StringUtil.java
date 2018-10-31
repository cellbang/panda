package org.malagu.panda.coke.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
  public static String find(String content, Pattern pattern, int index) {
    Matcher m = pattern.matcher(content);
    if (m.find()) {
      return m.group(1);
    } else {
      return null;
    }
  }

  public static String find(String content, String pattern, int index) {
    Pattern p = Pattern.compile(pattern);
    return find(content, p, index);
  }
}
