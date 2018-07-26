package org.malagu.panda.coke.utility;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JSONUtil {
  public static final String DEFAULT_DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
  public static final String IGNORE_PROPERTEIS_FILTER = "ignoreProperteisFilter";

  /**
   * 获取ObjectMapper
   * 
   * @return
   */
  public static ObjectMapper buildObjectMapper() {
    return buildObjectMapper(null);
  }

  public static ObjectMapper buildObjectMapper(String dateFormat) {
    if (dateFormat == null) {
      dateFormat = DEFAULT_DATE_FORMATE;
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat(dateFormat));
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  /**
   * JSON字符串转集合
   * 
   * @param json
   * @param clazz
   * @return
   */
  public static <T> List<T> jsonToList(String json, Class<T> clazz) {
    ObjectMapper mapper = buildObjectMapper();
    JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    try {
      return mapper.readValue(json, javaType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 对象转JSON字符串并格式化输出.
   * 
   * @param object
   * @return
   */
  public static String prettyJSON(Object object) {
    String json = null;
    ObjectMapper mapper = buildObjectMapper();
    try {
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return json;
  }


  /**
   * 对象转JSON字符串
   * 
   * @param object
   * @return
   */
  public static String toJSON(Object object) {
    return toJSON(object, DEFAULT_DATE_FORMATE);
  }


  public static String toJSON(Object object, String dateFormat) {

    ObjectMapper mapper = buildObjectMapper(dateFormat);
    StringWriter writer = new StringWriter();
    try {
      mapper.writeValue(writer, object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  /**
   * 对象转JSON字符串, 并忽略指定属性.
   * 
   * @param object
   * @param ignoreProperties
   * @return
   */
  public static String toJSON(Object object, String... ignoreProperties) {
    ObjectMapper mapper = buildObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.addMixIn(Object.class, PropertyFilterMixIn.class);
    FilterProvider filterProvider = new SimpleFilterProvider().addFilter(IGNORE_PROPERTEIS_FILTER,
        SimpleBeanPropertyFilter.serializeAllExcept(ignoreProperties));
    try {
      mapper.writer(filterProvider).writeValue(writer, object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  public static String toXML(Object object) {
    XmlMapper xmlMapper = new XmlMapper();
    try {
      return xmlMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  /**
   * 对象解析为JSON Node
   * 
   * @param obj
   * @return
   */
  public static JsonNode toJsonNode(Object obj) {
    if (obj == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    return mapper.valueToTree(obj);
  }

  /**
   * @param json
   * @return
   */
  public static JsonNode toJsonNode(String json) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    try {
      return mapper.readTree(json);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    try {
      JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
      return mapper.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Java对象转Map
   * 
   * @param obj
   * @return
   */
  public static Map<String, Object> toMap(Object obj) {
    ObjectMapper mapper = buildObjectMapper();
    JavaType javaType = mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class,
        Map.class, String.class, Object.class);
    Map<String, Object> props = mapper.convertValue(obj, javaType);
    return props;
  }



  /**
   * JSON字符串转Map
   * 
   * @param json
   * @return
   */
  public static Map<String, Object> toMap(String json) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    try {
      JavaType javaType = mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class,
          Map.class, String.class, Object.class);
      return mapper.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * JSON字符串转对象
   * 
   * @param json
   * @return
   */
  public static Object toObject(String json) {
    return toObject(json, Object.class);
  }

  /**
   * JSON字符串转指定类的实例.
   * 
   * @param json
   * @param clazz
   * @return
   */
  public static <T> T toObject(String json, Class<T> clazz) {
    ObjectMapper mapper = buildObjectMapper();
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * JSON TreeNode 转对象
   * 
   * @param treeNode
   * @return
   */
  public static Object toObject(TreeNode treeNode) {
    return toObject(treeNode, Object.class);
  }

  /**
   * JSON TreeNode 转 指定类的实例
   * 
   * @param treeNode
   * @param clazz
   * @return
   */
  public static <T> T toObject(TreeNode treeNode, Class<T> clazz) {
    ObjectMapper mapper = buildObjectMapper();
    try {
      return mapper.treeToValue(treeNode, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> xmlToMap(String xml) {
    if (xml == null) {
      return null;
    }
    JacksonXmlModule module = new JacksonXmlModule();
    XmlMapper xmlMapper = new XmlMapper(module);
    Map<String, Object> content = null;
    try {
      content = xmlMapper.readValue(xml, Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content;
  }
}


@JsonFilter(JSONUtil.IGNORE_PROPERTEIS_FILTER)
class PropertyFilterMixIn {
}
