package org.malagu.panda.coke.filestorage.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.malagu.panda.coke.filestorage.domain.CokeFileInfo;
import org.malagu.panda.coke.filestorage.domain.CokeFileShare;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.malagu.panda.coke.filestorage.service.FileStorageService;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  public CokeFileInfo put(MultipartFile file) throws IllegalStateException, IOException {
    return put(defaultFileStorageProviderType, file);
  }

  @Override
  public CokeFileInfo put(MultipartFile file, String filename)
      throws IllegalStateException, IOException {
    return put(defaultFileStorageProviderType, file);
  }

  @Override
  public CokeFileInfo put(String fileStorageType, InputStream inputStream, String filename)
      throws IOException {
    String relativePath = getFileStorageProvider(fileStorageType).put(inputStream, filename);
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

    try {
      buildAbsolutePath(fileInfo);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return fileInfo;
  }

  @Override
  public CokeFileInfo put(String fileStorageType, MultipartFile file)
      throws IllegalStateException, IOException {
    String relativePath = getFileStorageProvider(fileStorageType).put(file);
    return saveFile(fileStorageType, relativePath, file.getOriginalFilename());

  }

  @Override
  public CokeFileInfo get(String fileNo) throws FileNotFoundException {
    CokeFileInfo cokeFileInfo = JpaUtil.linq(CokeFileInfo.class).equal("fileNo", fileNo).findOne();

    return buildAbsolutePath(cokeFileInfo);
  }

  @Override
  public CokeFileInfo get(Integer fileId) throws FileNotFoundException {
    CokeFileInfo cokeFileInfo = JpaUtil.linq(CokeFileInfo.class).idEqual(fileId).findOne();
    return buildAbsolutePath(cokeFileInfo);
  }

  CokeFileInfo buildAbsolutePath(CokeFileInfo cokeFileInfo) throws FileNotFoundException {
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

  @Value("${coke.defaultFileStorageProviderType}")
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

  @SuppressWarnings("unchecked")
  @Override
  @Transactional
  public CokeFileInfo getByShareCode(String shareCode) throws FileNotFoundException {
    CokeFileInfo cokeFileInfo = null;
    String qlString = "select f from " + CokeFileInfo.class.getName() + " f, "
        + CokeFileShare.class.getName() + " s where f.id = s.fileId and s.shareCode = :shareCode";
    List<CokeFileInfo> list = JpaUtil.createEntityManager().createQuery(qlString)
        .setParameter("shareCode", shareCode).getResultList();
    if (list.size() > 0) {
      cokeFileInfo = list.get(0);
    }

    if (cokeFileInfo == null) {
      return null;
    }

    int fileCount = JpaUtil.nativeQuery(
        "update s  set s.balance_times = s.balance_times - 1 from  CK_FILE_SHARE s where s.share_code = :shareCode and s.balance_times > 0 and s.validate_date >= :now")
        .setParameter("shareCode", shareCode).setParameter("now", new Date()).executeUpdate();
    return fileCount > 0 ? cokeFileInfo : null;
  }

  @Override
  public CokeFileShare createShareCode(Long fileId) {
    return createShareCode(fileId, 3, 10);
  }

  @Override
  @Transactional
  public CokeFileShare createShareCode(Long fileId, int validateDay, Integer totalTimes) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, validateDay);

    CokeFileShare cokeFileShare = new CokeFileShare();
    cokeFileShare.setShareCode(UUID.randomUUID().toString());
    cokeFileShare.setValidateDate(calendar.getTime());
    cokeFileShare.setFileId(fileId);
    cokeFileShare.setBalanceTimes(totalTimes);
    cokeFileShare.setTotalTimes(totalTimes);
    JpaUtil.persist(cokeFileShare);
    return cokeFileShare;
  }

}
