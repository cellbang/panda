package org.malagu.panda.coke.filestorage.service;

import org.malagu.panda.coke.filestorage.domain.CokeBlob;

public interface CokeBlobService {
  CokeBlob put(byte[] data);

  CokeBlob get(Long id);

}
