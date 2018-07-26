package org.malagu.panda.coke.filestorage.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.coke.filestorage.domain.CokeFileInfo;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.malagu.panda.coke.filestorage.service.FileStorageService;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service(FileStorageService.BEAN_ID)
public class FileStorageServiceImpl implements FileStorageService {
  private Map<String, FileStorageProvider> fileStorageProviderMap =
      new HashMap<String, FileStorageProvider>();

  @Override
  public CokeFileInfo put(InputStream inputStream, String filename) throws IOException {
    return put(defaultFileStorageProviderType, inputStream, filename);

  }

  @Override
  public CokeFileInfo put(MultipartFile file, String filename)
      throws IllegalStateException, IOException {
    return put(defaultFileStorageProviderType, file, filename);
  }

  @Override
  public CokeFileInfo put(String fileStorageType, InputStream inputStream, String filename)
      throws IOException {
    String relativePath = getFileStorageProvider(fileStorageType).put(inputStream);
    return saveFile(fileStorageType, relativePath, filename);
  }

  public CokeFileInfo saveFile(String fileStorageType, String relativePath, String filename) {
    CokeFileInfo fileInfo = new CokeFileInfo();
    fileInfo.setFilename(filename);
    fileInfo.setCreateTime(new Date());
    fileInfo.setFileNo(UUID.randomUUID().toString());
    fileInfo.setFileStorageType(fileStorageType);
    fileInfo.setRelativePath(relativePath);
    JpaUtil.persist(fileInfo);
    return fileInfo;
  }

  @Override
  public CokeFileInfo put(String fileStorageType, MultipartFile file, String filename)
      throws IllegalStateException, IOException {
    String relativePath = getFileStorageProvider(fileStorageType).put(file);
    return saveFile(fileStorageType, relativePath, filename);

  }

  @Override
  public CokeFileInfo get(String fileNo) throws FileNotFoundException {
    CokeFileInfo cokeFileInfo = JpaUtil.linq(CokeFileInfo.class).equal("fileNo", fileNo).findOne();

    if (cokeFileInfo != null) {
      String absolutePath = getFileStorageProvider(cokeFileInfo.getFileStorageType())
          .getAbsolutePath(cokeFileInfo.getRelativePath());
      cokeFileInfo.setAbsolutePath(absolutePath);
    }
    return cokeFileInfo;
  }

  FileStorageProvider getFileStorageProvider(String fileStorageType) {
    FileStorageProvider fileStorageProvider = null;
    if (StringUtils.isEmpty(fileStorageType)) {
      fileStorageType = defaultFileStorageProviderType;
    }
    fileStorageProvider = fileStorageProviderMap.get(fileStorageType);
    if (fileStorageProvider == null) {
      throw new RuntimeException("unknow FileStrorageType " + fileStorageType);
    }
    return fileStorageProvider;
  }

  @Value("${coke.defaultFileStorageProviderType:}")
  private String defaultFileStorageProviderType;

  @Autowired
  public void setFileStorageProviderMap(Collection<FileStorageProvider> fileStorageProviders) {
    if (fileStorageProviders == null) {
      return;
    }

    for (FileStorageProvider fileStorageProvider : fileStorageProviders) {
      this.fileStorageProviderMap.put(fileStorageProvider.getType(), fileStorageProvider);
    }
  }

  @Override
  public InputStream getInputStream(String fileNo) throws FileNotFoundException {
    CokeFileInfo fileInfo = get(fileNo);
    if (fileInfo == null) {
      return null;
    }
    return getInputStream(fileInfo);
  }

  @Override
  public InputStream getInputStream(CokeFileInfo cokeFileInfo) throws FileNotFoundException {
    return getFileStorageProvider(cokeFileInfo.getFileStorageType())
        .getInputStream(cokeFileInfo.getRelativePath());
  }

  @Override
  public String getAbsolutePath(String fileNo) throws FileNotFoundException {
    CokeFileInfo cokeFileInfo = get(fileNo);
    return cokeFileInfo != null ? cokeFileInfo.getAbsolutePath() : null;
  }

}
