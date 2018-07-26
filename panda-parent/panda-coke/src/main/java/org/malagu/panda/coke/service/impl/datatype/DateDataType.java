/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) and BSDN
 * commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package org.malagu.panda.coke.service.impl.datatype;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import com.bstek.dorado.data.type.DataConvertException;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.DateUtils;

/**
 * 用于描述java.util.Date的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class DateDataType extends AbstractDataTypeConverter {
  private static String DATE_FORMAT_4 = "HH:mm:ss";
  private static String DATE_FORMAT_5 = "yyyy-MM-dd HH:mm:ss";

  private static int DATE_FORMAT_1_LEN = com.bstek.dorado.core.Constants.ISO_DATE_FORMAT.length();
  private static int DATE_FORMAT_2_LEN =
      com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1.length() - 4;
  private static int DATE_FORMAT_3_LEN =
      com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT2.length() - 4;
  private static int DATE_FORMAT_4_LEN = DATE_FORMAT_4.length();
  private static int DATE_FORMAT_5_LEN = DATE_FORMAT_5.length();

  @Override
  public String toText(Object value) {
    if (value == null) {
      return null;
    } else {
      Assert.isInstanceOf(Date.class, value);
      Date date = (Date) value;
      return String.valueOf(date.getTime());
    }
  }

  public Object fromText(String text) {
    if (StringUtils.isEmpty(text)) {
      return null;
    } else {
      return convertText2Date(text);
    }
  }

  /**
   * 尝试将一段文本转换成日期对象。
   * 
   * @param text 文本
   * @return 转换得到的日期对象
   * @throws ValueConvertException
   * @throws NumberFormatException
   */
  protected Date convertText2Date(String text) throws DataConvertException, NumberFormatException {
    if (NumberUtils.isNumber(text)) {
      long time = Long.parseLong(text);
      return new Date(time);
    } else {
      try {
        Date date = null;
        int len = text.length();
        try {
          if (len == DATE_FORMAT_1_LEN) {
            date = DateUtils.parse(com.bstek.dorado.core.Constants.ISO_DATE_FORMAT, text);
          } else if (len == DATE_FORMAT_2_LEN) {
            date = DateUtils.parse(com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1, text);
          } else if (len == DATE_FORMAT_3_LEN) {
            date = DateUtils.parse(com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT2, text);
          } else if (len == DATE_FORMAT_4_LEN) {
            date = DateUtils.parse(DATE_FORMAT_4, text);
          } else if (len == DATE_FORMAT_5_LEN) {
            date = DateUtils.parse(DATE_FORMAT_5, text);
          }
        } catch (ParseException ex) {
          // do nothing
        }

        if (date == null) {
          date = DateUtils.parse(text);
        }
        return date;
      } catch (ParseException ex) {
        throw new DataConvertException(text, Date.class);
      }
    }
  }

  @Override
  public Object fromObject(Object value) {
    if (value == null) {
      return null;
    } else {
      return convertObject2Date(value);
    }
  }

  /**
   * 尝试将一个任意对象转换成日期对象。
   * 
   * @param value 任意对象
   * @return 转换得到的日期对象
   * @throws ValueConvertException
   */
  protected Date convertObject2Date(Object value) throws DataConvertException {
    Class<?> targetType = getMatchType();
    if (targetType != null && targetType.isAssignableFrom(value.getClass())
        || targetType == null && value instanceof Date) {
      return (Date) value;
    } else if (value instanceof String) {
      return convertText2Date((String) value);
    } else if (value instanceof Long) {
      return new Date(((Long) value).longValue());
    } else {
      throw new DataConvertException(value.getClass(), getMatchType());
    }
  }

  @Override
  public Class<?> getMatchType() {
    return Date.class;
  }

}
