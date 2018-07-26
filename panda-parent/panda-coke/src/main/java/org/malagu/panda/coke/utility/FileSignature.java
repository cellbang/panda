package org.malagu.panda.coke.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class FileSignature {

  public static String sign(String path) {

    File file = new File(path);
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      MessageDigest messagedigest;
      messagedigest = MessageDigest.getInstance("SHA-1");

      byte[] buffer = new byte[1024 * 1024 * 10];
      int len = 0;

      while ((len = in.read(buffer)) > 0) {
        messagedigest.update(buffer, 0, len);
      }
      BigInteger bigInt = new BigInteger(1, messagedigest.digest());
      return bigInt.toString(16);
    } catch (Exception e) {
      e.printStackTrace();
      // throw new RuntimeException(e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    return null;
  }
}
