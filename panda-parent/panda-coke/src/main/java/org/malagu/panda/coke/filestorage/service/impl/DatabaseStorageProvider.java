package org.malagu.panda.coke.filestorage.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.malagu.panda.coke.filestorage.domain.CokeBlob;
import org.malagu.panda.coke.filestorage.service.CokeBlobService;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DatabaseStorageProvider implements FileStorageProvider {

  public static final String ProviderType = "Database";

  @Value("${coke.enableDatabaseLocalCache:true}")
  private boolean enableLocalFileCache;

  @Value("${coke.databaseStorageLocalCacheLocation:}")
  private String databaseCachedfileSystemStorageLocation;

  @Override
  public String getType() {
    return ProviderType;
  }

  @Override
  public String put(InputStream inputStream) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, baos);
    CokeBlob cokeBlob = cokeBlobService.put(baos.toByteArray());
    return cokeBlob.getId().toString();
  }

  @Override
  public String put(MultipartFile file)
      throws IllegalStateException, IOException {
    return put(file.getInputStream());
  }

  public String getAbsolutePath(String relativePath) throws FileNotFoundException {
    if (!enableLocalFileCache) {
      return null;
    }

    String path = null;
    File file = null;
    path = DigestUtils.md5Hex(relativePath);
    path = rebuildString(path, File.separator, 8, 4, 4, 4);
    file = new File(databaseCachedfileSystemStorageLocation + path);
    if (file.exists()) {
      return file.getAbsolutePath();
    }

    CokeBlob cokeBlob = cokeBlobService.get(Long.valueOf(relativePath));
    if (cokeBlob != null) {
      byte[] data = cokeBlob.getData();
      InputStream inputStream = new ByteArrayInputStream(data);
      File parent = file.getParentFile();
      if (!parent.exists()) {
        parent.mkdirs();
      }
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      try {
        IOUtils.copy(inputStream, fileOutputStream);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        IOUtils.closeQuietly(fileOutputStream);
      }
    } else {
      throw new FileNotFoundException("CokeBlob Record not found " + relativePath);
    }
    return file.getAbsolutePath();
  }

  @Override
  public InputStream getInputStream(String relativePath) throws FileNotFoundException {
    InputStream inputStream = null;
    String absolutePath = getAbsolutePath(relativePath);
    if (absolutePath != null) {
      inputStream = new FileInputStream(absolutePath);
    } else {
      CokeBlob cokeBlob = cokeBlobService.get(Long.valueOf(relativePath));
      if (cokeBlob != null) {
        byte[] data = cokeBlob.getData();
        inputStream = new ByteArrayInputStream(data);
      }

    }
    return inputStream;
  }

  static String rebuildString(String target, String separator, int... lengths) {
    int length = target.length();
    int builderLength = length + lengths.length * separator.length();
    StringBuilder builder = new StringBuilder(builderLength);
    int start = 0;
    for (int i : lengths) {
      builder.append(target.substring(start, start + i)).append(separator);
      start += i;
    }
    builder.append(target.substring(start));
    return builder.toString();
  }


  @Resource
  private CokeBlobService cokeBlobService;

}
