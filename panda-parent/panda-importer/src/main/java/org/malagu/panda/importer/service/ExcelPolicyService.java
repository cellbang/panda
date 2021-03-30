/**
 * 
 */
package org.malagu.panda.importer.service;

import java.io.InputStream;
import java.util.Map;

/**
 * @author xobo
 *
 */
public interface ExcelPolicyService {
  String EXCEL_HEADER_LIST = "EXCEL_HEADER_LIST";
  String MATCH_BY = "MATCH_BY";
  Integer MATCH_BY_LABEL = 2;

  boolean parse(InputStream inpuStream, Map<String, Object> parameter);
}
