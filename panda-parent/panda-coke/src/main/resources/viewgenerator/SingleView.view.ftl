<#import "/Entity.view.ftl" as my>
<?xml version="1.0" encoding="UTF-8"?>
<ViewConfig>
  <Arguments/>
  <Context/>
  <Model>
   	<@my.DataType name=entity.name entity=entity props=entity.props/>
   	<#if entity.queryProps?has_content>
   	<@my.DataType name="${entity.name}Query" entity=entity props=entity.queryProps/>
   	</#if>
  </Model>
  <View>
    <ClientEvent name="onReady">
    <@my.AutoAction entity=entity/>
    </ClientEvent>
    <Property name="packages">CokeAction</Property>
   	<@my.Entity entity=entity/>
  </View>
</ViewConfig>