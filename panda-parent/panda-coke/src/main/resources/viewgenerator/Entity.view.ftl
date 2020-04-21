 <#macro Entity entity>
     <#if entity.queryProps?has_content>
	 <Panel>
      <Property name="caption">查询</Property>
      <Buttons>
        <Button id="buttonQuery${entity.name}"/>
        <Button id="buttonQueryReset${entity.name}"/>
      </Buttons>
      <Children>
      	<#if entity.master>
        <DataSet id="${entity.dataSet}Query">
          <Property name="dataType">${entity.name}Query</Property>
        </DataSet>
        </#if>
        <AutoForm>
          <Property name="dataSet">${entity.dataSet}Query</Property>
          <Property name="labelAlign">right</Property>
 		  <#if entity.queryProps?has_content>
          <#list entity.queryProps as prop>
          <AutoFormElement>
            <Property name="name">${prop.name}</Property>
            <Property name="property">${prop.name}</Property>
            <Editor/>
          </AutoFormElement>
          </#list>
          </#if>
        </AutoForm>
      </Children>
      <Tools/>
    </Panel>
    </#if>
    <#if entity.master>
    <DataSet id="${entity.dataSet}">
      <Property name="dataType">[${entity.name}]</Property>
      <Property name="dataProvider">#load${entity.name}</Property>
      <Property name="pageSize">30</Property>
    </DataSet>
    </#if>
    <ToolBar>
      <ToolBarButton id="buttonInsert${entity.name}"/>
      <ToolBarButton id="buttonEdit${entity.name}"/>
      <ToolBarButton id="buttonDel${entity.name}"/>
    </ToolBar>
    <DataGrid id="dataGrid${entity.name}">
      <Property name="dataSet">${entity.dataSet}</Property>
      <#if entity.listDataPath?has_content>
      <Property name="dataPath">${entity.listDataPath}</Property>
      </#if>
      <Property name="readOnly">true</Property>
      <#list entity.props as prop>
      <DataColumn name="${prop.name}">
        <Property name="property">${prop.name}</Property>
      </DataColumn>
      </#list>
    </DataGrid>
    <Dialog id="dialog${entity.name}">
      <Property name="width">600</Property>
      <Property name="height">400</Property>
      <Buttons>
        <Button id="buttonSave${entity.name}"/>
        <Button id="buttonCancel${entity.name}"/>
      </Buttons>
      <Children>
        <AutoForm>
          <Property name="dataSet">${entity.dataSet}</Property>
          <#if entity.listDataPath?has_content>
      	  <Property name="dataPath">${entity.currentDataPath}</Property>
          </#if>
          <Property name="labelAlign">right</Property>
          <#list entity.props as prop>
          <AutoFormElement>
            <Property name="name">${prop.name}</Property>
            <Property name="property">${prop.name}</Property>
            <Editor/>
          </AutoFormElement>
          </#list>
        </AutoForm>
      </Children>
      <Tools/>
    </Dialog>
</#macro>

 <#macro DataType name entity props>
    <DataType name="${name}">
      <Property name="creationType">${entity.clazz}</Property>
      <#list props as prop>
        <PropertyDef name="${prop.name}">
      	<#if prop.name?has_content>
        <Property name="name">${prop.name}</Property>
      	</#if>
      	<#if prop.label?has_content>
        <Property name="label">${prop.label}</Property>
      	</#if>
      	<#if prop.dataType?has_content>
        <Property name="dataType">${prop.dataType}</Property>
      	</#if>
      	<#if prop.defaultValue?has_content>
        <Property name="defaultValue">${prop.defaultValue}</Property>
      	</#if>
      	<#if prop.displayFormat?has_content>
        <Property name="displayFormat">${prop.displayFormat}</Property>
      	</#if>
      	<#if prop.required?has_content>
        <Property name="required">${prop.required}</Property>
      	</#if>
      	<#if prop.tags?has_content>
        <Property name="tags">${prop.tags}</Property>
      	</#if>
      </PropertyDef>
      </#list>
      <#list entity.children as child>
      <Reference name="${child.listName}">
        <Property name="pageSize">30</Property>
        <Property name="dataType">[${child.name}]</Property>
        <Property name="dataProvider">#load${child.name}</Property>
        <Property name="parameter">
          <Entity>
          <#if child.parameterList?has_content>
		    <#list child.parameterList as parameter>
            <Property name="${parameter.name}">${parameter.value}</Property>
            </#list>
          </#if>
          </Entity>
        </Property>
      </Reference>
      </#list> 
    </DataType>
</#macro>

<#macro AutoAction entity>
coke.autoAction(view, {
<#if !entity.master>
	parentName: &quot;${entity.parentName}&quot;,
	currentPath: &quot;${entity.currentDataPath}&quot;,
	listPath: &quot;${entity.listDataPath}&quot;,
</#if>
	name: &quot;${entity.name}&quot;
});
</#macro>

<#macro UpdateAction entity>
    <UpdateAction id="updateAction${entity.name}">
      <Property name="dataResolver">#save${entity.name}</Property>
      <UpdateItem>
        <Property name="dataSet">dataSet${entity.name}</Property>
      </UpdateItem>
    </UpdateAction>
</#macro>
