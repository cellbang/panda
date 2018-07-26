package org.malagu.panda.coke.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

public class ClobUtil {
  static public String convertToString(Clob clob) throws SQLException, IOException {
    InputStream in = clob.getAsciiStream();
    StringWriter w = new StringWriter();
    IOUtils.copy(in, w);
    return w.toString();
  }

}
