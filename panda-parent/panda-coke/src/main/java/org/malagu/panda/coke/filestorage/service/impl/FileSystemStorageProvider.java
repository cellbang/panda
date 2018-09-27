package org.malagu.panda.coke.filestorage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageProvider implements FileStorageProvider {
  public static final String ProviderType = "FileSystem";

  @Value("${coke.fileSystemStorageLocation:fileStorage/}")
  private String fileSystemStorageLocation;

  @Override
  public String put(InputStream inputStream) throws IOException {
    String relativePath = getRelativPath();
    File targetFile = getTargetFile(fileSystemStorageLocation, relativePath);
    OutputStream os = new FileOutputStream(targetFile);
    IOUtils.copy(inputStream, os);
    IOUtils.closeQuietly(os);
    return relativePath;
  }

  @Override
  public String put(MultipartFile file) throws IllegalStateException, IOException {
    String relativePath = getRelativPath() + getFileSuffix(file.getOriginalFilename());
    File targetFile = getTargetFile(fileSystemStorageLocation, relativePath);
    file.transferTo(targetFile);

    return relativePath;
  }

  @Override
  public InputStream getInputStream(String relativePath) throws FileNotFoundException {
    File targetFile = getTargetFile(fileSystemStorageLocation, relativePath);
    return new FileInputStream(targetFile);
  }

  public static String getRelativPath() {
    String[] paths = UUID.randomUUID().toString().split("-");
    return StringUtils.join(paths, File.separatorChar);
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
  
  public static String getFileSuffix(String fileName){
    String suffix = "";
    int index = fileName.lastIndexOf(".");
    if(index != -1){
      suffix = fileName.substring(index);
    }
    return suffix;
  }
  
}
