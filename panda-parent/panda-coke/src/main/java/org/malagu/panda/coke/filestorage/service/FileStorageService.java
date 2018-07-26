package org.malagu.panda.coke.filestorage.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.malagu.panda.coke.filestorage.domain.CokeFileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  public static final String BEAN_ID = "coke.fileStorageService";

  CokeFileInfo put(InputStream inputStream, String filename) throws IOException;

  CokeFileInfo put(MultipartFile file, String filename) throws IllegalStateException, IOException;


  CokeFileInfo put(String fileStroageType, InputStream inputStream, String filename)
      throws IOException;

  CokeFileInfo put(String fileStroageType, MultipartFile file, String filename)
      throws IllegalStateException, IOException;

  CokeFileInfo get(String fileNo) throws FileNotFoundException;

  String getAbsolutePath(String fileNo) throws FileNotFoundException;

  InputStream getInputStream(String fileNo) throws FileNotFoundException;

  InputStream getInputStream(CokeFileInfo cokeFileInfo) throws FileNotFoundException;



}
