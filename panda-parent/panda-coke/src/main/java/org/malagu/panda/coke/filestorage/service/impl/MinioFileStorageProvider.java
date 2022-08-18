package org.malagu.panda.coke.filestorage.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.malagu.panda.coke.filestorage.service.FileStorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import io.minio.*;

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
        minioClient =
            MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        boolean isExist =
            minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!isExist) {
          // Make a new bucket called asiatrip to hold a zip file of photos.
          minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
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
  public String put(InputStream inputStream, String filename, String recommendRelativePath)
      throws IOException {
    try {
      minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(recommendRelativePath)
          .contentType("").build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return recommendRelativePath;
  }



  @Override
  public String put(MultipartFile file, String recommendRelativePath)
      throws IllegalStateException, IOException {
    return put(file.getInputStream(), file.getOriginalFilename(), recommendRelativePath);
  }

  @Override
  public InputStream getInputStream(String relativePath) throws FileNotFoundException {
    try {
      return minioClient
          .getObject(GetObjectArgs.builder().bucket(bucket).object(relativePath).build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getAbsolutePath(String relativePath) throws FileNotFoundException {
    return relativePath;
  }

}
