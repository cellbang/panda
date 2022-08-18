package org.malagu.panda.coke.filestorage.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageProvider {

  String getType();

  String put(InputStream inputStream, String filename, String recommendRelativePath) throws IOException;

  String put(MultipartFile file, String recommendRelativePath) throws IllegalStateException, IOException;

  InputStream getInputStream(String relativePath) throws FileNotFoundException;

  String getAbsolutePath(String relativePath) throws FileNotFoundException;
}
