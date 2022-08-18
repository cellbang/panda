package org.malagu.panda.coke.filestorage.service.impl;

import java.io.*;
import org.apache.commons.io.IOUtils;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageProvider implements FileStorageProvider {
  public static final String ProviderType = "FileSystem";

  @Value("${coke.fileSystemStorageLocation}")
  private String fileSystemStorageLocation;

  @Override
  public String put(InputStream inputStream, String filename, String recommendRelativePath)
      throws IOException {
    File targetFile = getTargetFile(fileSystemStorageLocation, recommendRelativePath);
    try (OutputStream os = new FileOutputStream(targetFile)) {
      IOUtils.copy(inputStream, os);
    }
    return recommendRelativePath;
  }

  @Override
  public String put(MultipartFile file, String recommendRelativePath)
      throws IllegalStateException, IOException {
    File targetFile = getTargetFile(fileSystemStorageLocation, recommendRelativePath);
    file.transferTo(targetFile);
    return recommendRelativePath;
  }

  @Override
  public InputStream getInputStream(String relativePath) throws FileNotFoundException {
    File targetFile = getTargetFile(fileSystemStorageLocation, relativePath);
    return new FileInputStream(targetFile);
  }


  public static File getTargetFile(String location, String relativePath) {
    String absolutePath = location + relativePath;
    File targetFile = new File(absolutePath);
    File parent = targetFile.getParentFile();
    if (parent != null && !parent.exists()) {
      parent.mkdirs();
    }
    return targetFile;
  }

  @Override
  public String getType() {
    return ProviderType;
  }

  @Override
  public String getAbsolutePath(String relativePath) throws FileNotFoundException {
    File targetFile = getTargetFile(fileSystemStorageLocation, relativePath);
    return targetFile.getAbsolutePath();
  }

}
