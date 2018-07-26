package org.malagu.panda.coke.filestorage.service.impl;

import java.util.Date;
import org.malagu.linq.JpaUtil;
import org.malagu.panda.coke.filestorage.domain.CokeBlob;
import org.malagu.panda.coke.filestorage.service.CokeBlobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CokeBlobServiceImpl implements CokeBlobService {

  @Override
  public CokeBlob put(byte[] data) {
    CokeBlob cokeBlob = new CokeBlob();
    cokeBlob.setData(data);
    cokeBlob.setCreateTime(new Date());
    JpaUtil.persist(cokeBlob);
    return cokeBlob;

  }

  @Override
  public CokeBlob get(Long id) {
    return JpaUtil.linq(CokeBlob.class).equal("id", id).findOne();

  }

}
