package org.malagu.panda.coke.filestorage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface TempFileService {

  Map<String, Object> getTmpfileInfo(HttpServletRequest request, String tmpfileNo);

  Map<String, Object> createTempFile(String prefix, String filename, byte[] data);

}
