package org.malagu.panda.coke.filestorage.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.malagu.panda.coke.filestorage.domain.CokeFileInfo;
import org.malagu.panda.coke.filestorage.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/file")
public class FileController {
  private Logger logger = LoggerFactory.getLogger(this.getClass());


  @RequestMapping(value = "/upload.k", produces = "application/json")
  public @ResponseBody Object handleFileUpload(HttpServletRequest request) throws IOException {

    boolean isMultipart = ServletFileUpload.isMultipartContent(request);

    Map<String, Object> result = new LinkedHashMap<String, Object>();

    Collection<Map<String, Object>> otherFiles = new ArrayList<Map<String, Object>>();
    if (isMultipart) {
      FileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);

      String fileStorageType = null;
      try {
        List<FileItem> items = upload.parseRequest(request);
        Iterator<FileItem> iterator = items.iterator();
        while (iterator.hasNext()) {
          FileItem item = (FileItem) iterator.next();
          if (item.isFormField() && "FileStorageType".equals(item.getFieldName())) {
            fileStorageType = item.getString();
          }
        }

        iterator = items.iterator();
        while (iterator.hasNext()) {
          FileItem item = (FileItem) iterator.next();

          if (!item.isFormField()) {
            String fileName = item.getName();
            CokeFileInfo cokeFileInfo =
                fileService.put(fileStorageType, item.getInputStream(), fileName);

            if ("file".equals(item.getFieldName())) {
              putFileInfo(cokeFileInfo, result);
            } else {
              Map<String, Object> otherFile = new LinkedHashMap<String, Object>();
              putFileInfo(cokeFileInfo, otherFile);
              otherFiles.add(otherFile);
            }
          }
        }
      } catch (FileUploadException e) {
        e.printStackTrace();
      }
    }

    if (!otherFiles.isEmpty()) {
      result.put("files", otherFiles);
    }
    return result;
  }

  void putFileInfo(CokeFileInfo cokeFileInfo, Map<String, Object> result) {
    result.put("fileNo", cokeFileInfo.getFileNo());
    result.put("fileName", cokeFileInfo.getFilename());
  }

  @RequestMapping(value = "/download/{fileNo}.k")
  public void download(@PathVariable("fileNo") String fileNo, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    CokeFileInfo cokeFileInfo = fileService.get(fileNo);

    if (cokeFileInfo == null) {
      findNotFound(response, fileNo);
      return;
    }


    HttpSession session = request.getSession();

    String filename = cokeFileInfo.getFilename();
    response.setContentType(session.getServletContext().getMimeType(filename));

    String encodFilename = URLEncoder.encode(filename, "utf-8");
    response.setHeader("Cache-Control", "max-age=31556926");
    response.setHeader("Content-Disposition",
        String.format("attachment; filename=\"%1$s\"; filename*=utf-8''%1$s", encodFilename));

    InputStream inputStream = fileService.getInputStream(cokeFileInfo);
    if (inputStream == null) {
      findNotFound(response, fileNo);
    }

    OutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();
      IOUtils.copy(inputStream, outputStream);
    } catch (FileNotFoundException fne) {
    } finally {
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(outputStream);
    }
  }

  void findNotFound(HttpServletResponse response, String fileNo) throws IOException {
    logger.info("File {} Not Found ", fileNo);
    response.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  @Resource(name = FileStorageService.BEAN_ID)
  private FileStorageService fileService;

}
