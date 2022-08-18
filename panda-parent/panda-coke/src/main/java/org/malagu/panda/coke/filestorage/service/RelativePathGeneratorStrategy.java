package org.malagu.panda.coke.filestorage.service;

public interface RelativePathGeneratorStrategy {

  String generate(String filename);

  String getName();

}
