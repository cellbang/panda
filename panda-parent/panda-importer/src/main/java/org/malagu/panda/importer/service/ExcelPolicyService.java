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

  boolean parse(InputStream inpuStream, Map<String, Object> parameter);
}
