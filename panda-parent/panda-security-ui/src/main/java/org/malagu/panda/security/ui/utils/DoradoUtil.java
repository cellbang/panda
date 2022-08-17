package org.malagu.panda.security.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import com.bstek.dorado.config.ExpressionMethodInterceptor;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.Mapping;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.util.proxy.BaseMethodInterceptorDispatcher;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.data.DataSet;
import com.bstek.dorado.view.widget.datacontrol.DataControl;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

public class DoradoUtil {
  public static EntityDataType getEntityDataType(View view, DataControl dataControl) {
    String dataSetId = dataControl.getDataSet();
    DataSet dataSet = (DataSet) view.getViewElement(dataSetId);
    DataType dataType = dataSet.getDataType();

    EntityDataType entityDataType = getEntityDataType(dataType);
    String dataPath = dataControl.getDataPath();


    if (StringUtils.isNotEmpty(dataPath) && entityDataType != null) {
      entityDataType = findEntityDataTypeByDataPath(entityDataType, dataPath);
    }
    return entityDataType;
  }

  public static EntityDataType findEntityDataTypeByDataPath(EntityDataType entityDataType,
      String dataPath) {
    String[] properties = null;
    if (StringUtils.isNotEmpty(dataPath)) {
      dataPath = dataPath.replaceAll("#", "");
      dataPath = dataPath.replaceAll("!\\w+", "");
      properties = dataPath.split("\\.");
    }

    if (properties != null) {
      for (String protpery : properties) {
        PropertyDef propertyDef = entityDataType.getPropertyDef(protpery);
        if (propertyDef != null) {
          entityDataType = getEntityDataType(propertyDef.getDataType());
          if (entityDataType == null) {
            break;
          }
        } else {
          break;
        }
      }
    }

    return entityDataType;
  }

  /**
   * 从 entityDataType 中提取指定属性的中文标签
   * 
   * @param entityDataType
   * @param property
   * @return
   */
  public static String getLabel(EntityDataType entityDataType, String property) {
    if (StringUtils.isEmpty(property)) {
      return null;
    }
    if (!property.contains(".")) {
      PropertyDef propertyDef = entityDataType.getPropertyDef(property);
      if (propertyDef != null) {
        return buildLabel(propertyDef);
      }
    } else {
      int lastIndexOfDot = property.lastIndexOf('.');
      String dataPath = property.substring(0, lastIndexOfDot);
      String prop = property.substring(lastIndexOfDot + 1);

      EntityDataType entityDataType2 = findEntityDataTypeByDataPath(entityDataType, dataPath);
      if (entityDataType2 != null) {
        return getLabel(entityDataType2, prop);
      }
    }
    return null;

  }


  /**
   * 构建中文描述
   * 
   * @param propertyDef
   * @return
   */
  public static String buildLabel(PropertyDef propertyDef) {
    if (propertyDef == null) {
      return null;
    }

    String label = propertyDef.getLabel();

    String mappingCode = getMappingCode(propertyDef.getMapping());
    if (mappingCode != null) {
      label += "-" + mappingCode;
    }

    return label;


  }

  /**
   * 基于Mapping对象提取表达式
   * 
   * @param mapping
   * @return
   */
  public static String getMappingCode(Mapping mapping) {
    if (!(mapping instanceof ProxyObject)) {
      return null;
    }

    MethodHandler methodHandler = ((ProxyObject) mapping).getHandler();
    if (!(methodHandler instanceof BaseMethodInterceptorDispatcher)) {
      return null;
    }

    BaseMethodInterceptorDispatcher baseMethodInterceptorDispatcher =
        (BaseMethodInterceptorDispatcher) methodHandler;

    MethodInterceptor[] methodInterceptors =
        baseMethodInterceptorDispatcher.getSubMethodInterceptors();

    if (methodInterceptors == null || methodInterceptors.length <= 0) {
      return null;
    }

    for (MethodInterceptor methodInterceptor : methodInterceptors) {
      if (methodInterceptor instanceof ExpressionMethodInterceptor) {
        ExpressionMethodInterceptor expressionMethodInterceptor =
            (ExpressionMethodInterceptor) methodInterceptor;

        String mapValues =
            MapUtils.getString(expressionMethodInterceptor.getExpressionProperties(), "mapValues");

        if (StringUtils.isNotEmpty(mapValues)) {
          return extractCode(mapValues);
        }

      }
    }

    return null;
  }

  // ${dict.items("MyCompany")}
  private static Pattern DICT_PATTERN =
      Pattern.compile("\\$\\{dict.items\\([\"'](.+)[\"']\\)\\}", Pattern.CASE_INSENSITIVE);

  private static String extractCode(String mapValues) {
    Matcher matcher = DICT_PATTERN.matcher(mapValues);

    if (matcher.find()) {
      return matcher.group(1);
    }

    return mapValues;
  }


  public static EntityDataType getEntityDataType(DataType dataType) {
    EntityDataType entityDataType = null;
    if (dataType instanceof AggregationDataType) {
      dataType = ((AggregationDataType) dataType).getElementDataType();

    }
    if (dataType instanceof EntityDataType) {
      entityDataType = (EntityDataType) dataType;
    }
    return entityDataType;
  }

  public static class AutoFormDataControlWrapper implements DataControl {

    public AutoFormDataControlWrapper(AutoForm autoForm) {
      this.autoForm = autoForm;
    }

    private AutoForm autoForm;

    @Override
    public String getDataSet() {
      return autoForm.getDataSet();
    }

    @Override
    public void setDataSet(String dataSet) {
      autoForm.setDataSet(dataSet);
    }

    @Override
    public String getDataPath() {
      return autoForm.getDataPath();
    }

    @Override
    public void setDataPath(String dataPath) {
      autoForm.setDataPath(dataPath);
    }

  }

}
