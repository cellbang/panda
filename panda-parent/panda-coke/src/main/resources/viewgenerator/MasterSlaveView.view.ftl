<#import "/Entity.view.ftl" as my>
<?xml version="1.0" encoding="UTF-8"?>
<ViewConfig>
  <Arguments/>
  <Context/>
  <Model>
   	<@my.DataType name=master.name entity=master props=master.props/>
   	<#if master.queryProps?has_content>
   	<@my.DataType name="${master.name}Query" entity=master props=master.queryProps/>
   	</#if>
   	<@my.DataType name=slave.name entity=slave props=slave.props/>
   	<#if slave.queryProps?has_content>
   	<@my.DataType name="${slave.name}Query" entity=slave props=slave.queryProps/>
   	</#if>
  </Model>
  <View>
    <ClientEvent name="onReady">
    <@my.AutoAction entity=master/>
    <@my.AutoAction entity=slave/>
    </ClientEvent>
    <Property name="packages">CokeAction</Property>
    <Panel layoutConstraint="left">
      <Property name="width">50%</Property>
      <Buttons/>
      <Children>
      <@my.Entity entity=master/>
      </Children>
      <Tools/>
    </Panel>
    <Panel layoutConstraint="right">
      <Property name="width">50%</Property>
      <Buttons/>
      <Children>
      <@my.Entity entity=slave/>
      </Children>
      <Tools/>
    </Panel>
    <@my.UpdateAction entity=master/>
  </View>
</ViewConfig>