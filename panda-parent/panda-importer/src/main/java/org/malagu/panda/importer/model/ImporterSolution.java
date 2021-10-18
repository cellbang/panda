package org.malagu.panda.importer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.bstek.dorado.annotation.PropertyDef;

/**
 * @author Kevin.yang
 * @since 2015年8月22日
 */
@Entity
@Table(name = "PANDA_IMPORTER_SOLUTION")
public class ImporterSolution implements java.io.Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID_", length = 64)
  @PropertyDef(label = "方案编码")
  private String id;

  @PropertyDef(label = "方案名称")
  @Column(name = "NAME_", length = 60)
  private String name;

  @PropertyDef(label = "匹配方式", description = "1.列优先; 2.表头优先")
  @Column(name = "match_type_")
  private Integer matchType;

  @PropertyDef(label = "Sheet页名称")
  @Column(name = "EXCEL_SHEET_NAME_", length = 60)
  private String excelSheetName;

  @PropertyDef(label = "表头起始行")
  @Column(name = "START_ROW_HEADER_", length = 60)
  private Integer startRowHeader;

  @PropertyDef(label = "数据起始行")
  @Column(name = "START_ROW_DATA_", length = 60)
  private Integer startRowData;

  @Column(name = "ENTITY_CLASS_NAME_", length = 255)
  @PropertyDef(label = "实体类")
  private String entityClassName;

  @PropertyDef(label = "描述")
  @Column(name = "DESC_", length = 255)
  private String desc;

  @PropertyDef(label = "实体管理工厂")
  @Column(name = "Entity_Manager_Factory_Name_", length = 60)
  private String EntityManagerFactoryName;

  @Column(name = "CREATE_DATE_")
  @PropertyDef(label = "创建时间")
  private Date createDate;

  @PropertyDef(label = "解析后处理器")
  @Column(name = "POST_PROCESS_BEAN")
  private String postProcessBean;

  @OneToMany
  @JoinColumn(name = "IMPORTER_SOLUTION_ID_", insertable = false, updatable = false)
  private List<MappingRule> mappingRules = new ArrayList<MappingRule>();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExcelSheetName() {
    return excelSheetName;
  }

  public void setExcelSheetName(String excelSheetName) {
    this.excelSheetName = excelSheetName;
  }

  public String getEntityClassName() {
    return entityClassName;
  }

  public void setEntityClassName(String entityClassName) {
    this.entityClassName = entityClassName;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getEntityManagerFactoryName() {
    return EntityManagerFactoryName;
  }

  public void setEntityManagerFactoryName(String entityManagerFactoryName) {
    EntityManagerFactoryName = entityManagerFactoryName;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public List<MappingRule> getMappingRules() {
    return mappingRules;
  }

  public void setMappingRules(List<MappingRule> mappingRules) {
    this.mappingRules = mappingRules;
  }

  public String getPostProcessBean() {
    return postProcessBean;
  }

  public void setPostProcessBean(String postProcessBean) {
    this.postProcessBean = postProcessBean;
  }

  public Integer getMatchType() {
    return matchType;
  }

  public void setMatchType(Integer matchType) {
    this.matchType = matchType;
  }

  public Integer getStartRowHeader() {
    return startRowHeader;
  }

  public void setStartRowHeader(Integer startRowHeader) {
    this.startRowHeader = startRowHeader;
  }

  public Integer getStartRowData() {
    return startRowData;
  }

  public void setStartRowData(Integer startRowData) {
    this.startRowData = startRowData;
  }

}
