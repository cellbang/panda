package org.malagu.panda.coke.filestorage.service;

import java.io.IOException;
import java.io.InputStream;

public interface RelativePathGeneratorService {
  String generate(InputStream inputStream, String filename) throws IOException;
}
