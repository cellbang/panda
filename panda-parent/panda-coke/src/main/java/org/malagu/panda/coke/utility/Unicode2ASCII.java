package org.malagu.panda.coke.utility;

public class Unicode2ASCII {

  public static String toHTML(String unicode) {
    String output = "";

    char[] charArray = unicode.toCharArray();

    for (int i = 0; i < charArray.length; ++i) {
      char a = charArray[i];
      if (a > 255) {
        output += "&#" + (int) a + ";";
      } else {
        output += a;
      }
    }
    return output;
  }

  public static String toJAVA(String unicode) {
    String output = "";

    char[] charArray = unicode.toCharArray();

    for (int i = 0; i < charArray.length; ++i) {
      char a = charArray[i];
      if (a > 255) {
        output += "\\u" + Integer.toHexString(a);
      } else {
        output += a;
      }
    }

    return output;
  }
}
