package org.malagu.panda.coke.filestorage.service.impl;

import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.malagu.panda.coke.filestorage.service.RelativePathGeneratorStrategy;
import org.springframework.stereotype.Service;

@Service
public class DateUUIDFileNameRelativePathGeneratorStrategy
    implements RelativePathGeneratorStrategy {
  private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd/HHmmssSSS");

  @Override
  public String generate(String filename) {
    return dateFormat.format(new Date()) + "_" + UUID.randomUUID().toString() + "."
        + FilenameUtils.getExtension(filename);
  }

  @Override
  public String getName() {
    return "DateUUID";
  }
}
