package org.malagu.panda.coke.filestorage.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.malagu.panda.coke.filestorage.service.TempFileService;
import org.springframework.stereotype.Service;

import com.bstek.dorado.web.DoradoContext;
import com.google.common.collect.Maps;

@Service
public class TempFileServiceImpl implements TempFileService {
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> getTmpfileInfo(HttpServletRequest request, String tmpfileNo) {
    return (Map<String, Object>) DoradoContext.getAttachedRequest().getSession()
        .getAttribute("tmpfile:" + tmpfileNo);
  }

  @Override
  public Map<String, Object> createTempFile(String prefix, String filename, byte[] data) {
    Path path = null;
    try {
      path = Files.createTempFile(prefix, ".tmp");
      Files.write(path, data);
      path.toFile().deleteOnExit();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String uuid = UUID.randomUUID().toString();
    Map<String, Object> resultMap = Maps.newHashMap();
    resultMap.put("fileNo", uuid);
    resultMap.put("path", path.toString());
    resultMap.put("filename", filename);
    DoradoContext.getAttachedRequest().getSession().setAttribute("tmpfile:" + uuid, resultMap);
    return resultMap;
  }
}
