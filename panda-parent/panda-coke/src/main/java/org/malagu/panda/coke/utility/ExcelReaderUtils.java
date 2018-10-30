package org.malagu.panda.coke.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtils {
  private static Logger logger = Logger.getLogger(ExcelReaderUtils.class);

  private final static String xls = "xls";
  private final static String xlsx = "xlsx";

  @SuppressWarnings("unchecked")
  public static <T> T create(Class<T> clazz) {
    if (Map.class.equals(clazz)) {
      clazz = (Class<T>) HashMap.class;
    }
    try {
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Workbook getWorkBook(File file) {
    // 获得文件名
    String fileName = file.getName();
    // 创建Workbook工作薄对象，表示整个excel
    Workbook workbook = null;
    try {
      // 获取excel文件的io流
      InputStream is = new FileInputStream(file);
      // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
      if (fileName.endsWith(xls)) {
        // 2003
        workbook = new HSSFWorkbook(is);
      } else if (fileName.endsWith(xlsx)) {
        // 2007
        workbook = new XSSFWorkbook(is);
      }
    } catch (IOException e) {
      logger.info(e.getMessage());
    }
    return workbook;
  }

  public static Workbook getWorkBook(InputStream is, String fileName) {
    // 获得文件名
    // 创建Workbook工作薄对象，表示整个excel
    Workbook workbook = null;
    try {
      // 获取excel文件的io流
      // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
      if (fileName.endsWith(xls)) {
        // 2003
        workbook = new HSSFWorkbook(is);
      } else if (fileName.endsWith(xlsx)) {
        // 2007
        workbook = new XSSFWorkbook(is);
      }
    } catch (IOException e) {
      logger.info(e.getMessage());
    }
    return workbook;
  }

  public static List<List<String>> toList(File file) throws IOException {
    return toList(file, 0, 1);
  }

  public static List<List<String>> toList(File file, int sheetIndex, int startIndex)
      throws IOException {
    return toList(file, sheetIndex, startIndex, -1);
  }

  public static List<List<String>> toList(File file, int sheetIndex, int startIndex, int endIndex)
      throws IOException {
    try (InputStream is = new FileInputStream(file)) {
      Workbook workbook = getWorkBook(is, file.getName());
      return toList(workbook, sheetIndex, startIndex, endIndex);
    }
  }

  public static List<List<String>> toList(String filename, InputStream is, int sheetIndex,
      int startIndex, int endIndex)
      throws IOException {
    Workbook workbook = getWorkBook(is, filename);
    return toList(workbook, sheetIndex, startIndex, endIndex);
  }


  /**
   * 读入excel文件，解析后返回
   * 
   * @param endIndex TODO
   * @param file
   * 
   * @throws IOException
   */
  public static List<List<String>> toList(Workbook workbook, int sheetIndex, int startIndex,
      int endIndex)
      throws IOException {
    // 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
    List<List<String>> sheetList = new ArrayList<>();
    if (workbook != null) {
      // 获得当前sheet工作表
      Sheet sheet = workbook.getSheetAt(sheetIndex);
      if (sheet == null) {
        return Collections.emptyList();
      }
      // 获得当前sheet的结束行
      int lastRowNum = endIndex > 0 ? endIndex : sheet.getLastRowNum();
      // 循环除了第一行的所有行
      for (int rowNum = startIndex; rowNum <= lastRowNum; rowNum++) {
        // 获得当前行
        Row row = sheet.getRow(rowNum);
        if (row == null) {
          continue;
        }
        // 获得当前行的开始列
        int firstCellNum = row.getFirstCellNum();
        // 获得当前行的列数
        int lastCellNum = row.getPhysicalNumberOfCells();
        List<String> rowValues = new ArrayList<>(lastCellNum);
        // 循环当前行
        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
          Cell cell = row.getCell(cellNum);
          rowValues.add(ExcelUtils.getCellValue(cell));
        }
        sheetList.add(rowValues);
      }
    }
    return sheetList;
  }

  @SuppressWarnings("rawtypes")
  public static List<Map> toMap(File file, int sheetIndex, int startIndex, int endIndex,
      String[] properties)
      throws IOException {
    return toPOJO(file, sheetIndex, startIndex, endIndex, Map.class, properties);
  }

  @SuppressWarnings("rawtypes")
  public static List<Map> toMap(File file, String[] properties)
      throws IOException {
    return toMap(file, 0, 1, -1, properties);
  }


  public static <T> List<T> toPOJO(File file, int sheetIndex, int startIndex,
      int endIndex, Class<T> clazz, Map<Integer, String> propertyMap)
      throws IOException {

    List<List<String>> sheetList = null;
    try (InputStream is = new FileInputStream(file)) {
      Workbook workbook = getWorkBook(is, file.getName());
      sheetList = toList(workbook, sheetIndex, startIndex, endIndex);
    }

    if (CollectionUtils.isEmpty(sheetList)) {
      return Collections.emptyList();
    }

    List<T> objList = new ArrayList<>(sheetList.size());

    for (List<String> list : sheetList) {
      T targetObj = create(clazz);
      objList.add(targetObj);


      for (int i = 0; i < list.size(); i++) {
        String property = propertyMap.get(i);
        if (StringUtils.isNotEmpty(property)) {
          try {
            BeanUtils.setProperty(targetObj, property, list.get(i));
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }

    return objList;
  }

  public static <T> List<T> toPOJO(File file, int sheetIndex, int startIndex, int endIndex,
      Class<T> clazz, String[] properties) throws IOException {
    Map<Integer, String> propertyMap = new HashMap<>();
    for (int i = 0; i < properties.length; i++) {
      propertyMap.put(i, properties[i]);
    }
    return toPOJO(file, sheetIndex, startIndex, endIndex, clazz, propertyMap);
  }


}
