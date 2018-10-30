package org.malagu.panda.coke.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtils {
  public final static String DATE_OUTPUT_PATTERNS = "yyyy-MM-dd HH:mm:ss";

  public static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

  @SuppressWarnings("unused")
  public static boolean is2003Excel(FileInputStream in) {
    try {
      HSSFWorkbook hBook = new HSSFWorkbook(in);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  @SuppressWarnings("unused")
  public static boolean is2007Excel(FileInputStream in) {
    try {
      XSSFWorkbook xBook = new XSSFWorkbook(in);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public static String getCellValue(Cell cell) {
    String ret = "";
    if (cell == null) {
      return ret;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK:
        ret = "";
        break;
      case Cell.CELL_TYPE_BOOLEAN:
        ret = String.valueOf(cell.getBooleanCellValue());
        break;
      case Cell.CELL_TYPE_ERROR:
        ret = null;
        break;
      case Cell.CELL_TYPE_FORMULA:
        Workbook wb = cell.getSheet().getWorkbook();
        CreationHelper crateHelper = wb.getCreationHelper();
        FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
        ret = getCellValue(evaluator.evaluateInCell(cell));
        break;
      case Cell.CELL_TYPE_NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          Date theDate = cell.getDateCellValue();
          DateFormat format = new SimpleDateFormat(DATE_OUTPUT_PATTERNS);
          ret = format.format(theDate);
        } else {
          ret = NumberToTextConverter.toText(cell.getNumericCellValue());
        }
        break;
      case Cell.CELL_TYPE_STRING:
        ret = cell.getRichStringCellValue().getString();
        break;
      default:
        ret = null;
    }

    return null != ret ? ret.trim() : null; // 有必要自行trim
  }

  /**
   * 读取file为excel
   * 
   * @param file
   * @return
   */
  public static Workbook getWookBook(String path) {
    if (path == null || !(path.endsWith(".xls") || path.endsWith(".xlsx"))) {
      throw new RuntimeException("文件不是标准的excel格式");
    }
    Workbook workbook = null;
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(new File(path));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage());
    }
    try {
      if (path.endsWith(".xls")) {
        workbook = new HSSFWorkbook(inputStream);
      } else if (path.endsWith(".xlsx")) {
        workbook = new XSSFWorkbook(inputStream);
      }
    } catch (Exception e) {
      throw new RuntimeException("文件不是标准的excel格式");
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
    return workbook;
  }

  /**
   * excel删除sheet
   * 
   * @param path
   * @param sheetName
   */
  public static void deleteSheet(String path, String sheetName) {
    Workbook workbook = null;
    OutputStream outputStream = null;
    try {
      workbook = getWookBook(path);
      int sheetIndex = workbook.getSheetIndex(sheetName);
      workbook.removeSheetAt(sheetIndex);
      outputStream = new FileOutputStream(path);
      workbook.write(outputStream);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    } finally {
      IOUtils.closeQuietly(outputStream);
    }
  }
}
