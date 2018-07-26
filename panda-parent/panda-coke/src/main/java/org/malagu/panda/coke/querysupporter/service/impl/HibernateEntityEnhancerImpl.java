package org.malagu.panda.coke.querysupporter.service.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.malagu.panda.coke.querysupporter.service.ReflectionRegister;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bstek.dorado.core.EngineStartupListener;

/**
 * 加载Hibernate实体的所有属性
 * 
 * @author bing
 * 
 */
@Service
public class HibernateEntityEnhancerImpl extends EngineStartupListener {

	private static final Logger logger = LoggerFactory.getLogger(HibernateEntityEnhancerImpl.class);

	@Autowired
	private Collection<ReflectionRegister> reflectionRegisters;

	@Override
	public void onStartup() throws Exception {
		logger.info("analyze hibernate entity");
		Metamodel metamodel = JpaUtil.getEntityManagerFactory().getMetamodel();

		Set<EntityType<?>> entityTypes = metamodel.getEntities();
		for (EntityType<?> entityType : entityTypes) {
			Class<?> clazz = entityType.getJavaType();

			AbstractEntityPersister persister = null;
			if (metamodel instanceof MetamodelImplementor) {
				EntityPersister entityPersister = ((MetamodelImplementor) metamodel).entityPersister(clazz);
				if (entityPersister instanceof AbstractEntityPersister) {
					persister = (AbstractEntityPersister) entityPersister;
				}
			}
			// String tableName = persister.getTableName();

			Set<?> attributes = entityType.getAttributes();
			for (Object object : attributes) {
				Attribute<?, ?> attribute = (Attribute<?, ?>) object;
				Member member = attribute.getJavaMember();
				String name = attribute.getName();
				String columnName = persister.getPropertyColumnNames(name)[0];

				for (ReflectionRegister register : reflectionRegisters) {
					register.register(clazz, name, (AccessibleObject) member, columnName);
				}
			}

		}

	}

	@Override
	public int getOrder() {
		return 10000;
	}

}
