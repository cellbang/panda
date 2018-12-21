package org.malagu.panda.coke.filestorage.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.time.FastDateFormat;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import io.minio.MinioClient;

@Service
@ConditionalOnClass(MinioClient.class)
public class MinioFileStorageProvider implements FileStorageProvider {
  @Value("${coke.minio.endpoint}")
  private String endpoint;
  @Value("${coke.minio.accessKey}")
  private String accessKey;
  @Value("${coke.minio.secretKey}")
  private String secretKey;
  @Value("${coke.minio.bucket}")
  private String bucket;

  private MinioClient minioClient;

  private Logger logger = LoggerFactory.getLogger(MinioFileStorageProvider.class);

  @PostConstruct
  public void init() {
    try {
      if (ClassUtils.isPresent("io.minio.MinioClient", this.getClass().getClassLoader())) {
        minioClient = new MinioClient(endpoint, accessKey, secretKey);
        boolean isExist = minioClient.bucketExists(bucket);
        if (!isExist) {
          // Make a new bucket called asiatrip to hold a zip file of photos.
          minioClient.makeBucket(bucket);
        }
      }
    } catch (Exception e) {
      logger.error("MinioFileStorageProvider init failed ", e);

    }
  }

  @Override
  public String getType() {
    return "minio";
  }

  @Override
  public String put(InputStream inputStream, String filename) throws IOException {
    String relativePath = getRelativeDirectory() + filename;
    try {
      minioClient.putObject(bucket, relativePath, inputStream,
          "application/octet-stream");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public String getRelativeDirectory() {
    return datePath.format(new Date());
  }

  private FastDateFormat datePath = FastDateFormat.getInstance("yyyy/MM/");

  @Override
  public String put(MultipartFile file) throws IllegalStateException, IOException {
    return put(file.getInputStream(), file.getOriginalFilename());
  }

  @Override
  public InputStream getInputStream(String relativePath) throws FileNotFoundException {
    try {
      return minioClient.getObject(bucket, relativePath);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getAbsolutePath(String relativePath) throws FileNotFoundException {
    return null;
  }

}
