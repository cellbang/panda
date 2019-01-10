 package org.malagu.panda.dorado.linq.lin.impl;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang.ArrayUtils;
import org.malagu.linq.lin.impl.LinImpl;
import org.malagu.linq.transform.ResultTransformer;
import org.malagu.linq.transform.impl.Transformers;
import org.malagu.panda.dorado.linq.BeanUtils;
import org.malagu.panda.dorado.linq.CollectInfo;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.filter.Filter;
import org.malagu.panda.dorado.linq.filter.impl.BackfillFilter;
import org.malagu.panda.dorado.linq.lin.Linq;
import org.malagu.panda.dorado.linq.parser.CriterionParser;
import org.malagu.panda.dorado.linq.parser.SmartSubQueryParser;
import org.malagu.panda.dorado.linq.parser.SubQueryParser;
import org.malagu.panda.dorado.linq.policy.LinqContext;
import org.malagu.panda.dorado.linq.policy.impl.QBCCriteriaContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

import net.sf.cglib.beans.BeanMap;

/**
 *@author Kevin.yang
 *@since 2015年6月10日
 */
public class LinqImpl extends LinImpl<Linq, CriteriaQuery<?>> implements Linq {
	private Criteria c;
	private boolean toEntity;
	private List<CollectInfo> collectInfos = new ArrayList<CollectInfo>();
	private Map<Class<?>, String[]> projectionMap = new HashMap<Class<?>, String[]>();
	private LinqContext linqContext = new LinqContext();
	private List<CriterionParser> criterionParsers = new ArrayList<CriterionParser>();

	private Filter filter;
	private boolean disableSmartSubQueryCriterion;
	private boolean disableBackFillFilter; 		
	protected List<Order> orders = new ArrayList<Order>();
	protected boolean distinct;
	protected Class<?> resultClass;
	protected ResultTransformer resultTransformer;

	
	
	@Override
	public Linq where(Criteria criteria) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.c = criteria;
		return this;
	}

	
	@Override
	public Linq filter(Filter filter) {
		this.filter = filter;
		return this;
	}
	
	protected void beforeExecute(AbstractQuery<?> query) {
		if (!disableSmartSubQueryCriterion && c != null) {
			this.addParser(new SmartSubQueryParser(this, domainClass, collectInfos));
		}
		doParseCriteria();
		applyPredicateToCriteria(query);
	}

	protected void afterExecute(Collection<?> entities) {
		doCollect(entities);
		doFilter(entities);
	}
	
	protected void doParseCriteria() {
		if (c != null) {
			QBCCriteriaContext context = new QBCCriteriaContext();
			context.setCriteria(c);
			context.setEntityClass(domainClass);
			context.setLinq(this);
			context.setCriterionParsers(criterionParsers);
			JpaUtil.getDefaultQBCCriteriaPolicy().apply(context);
		}
	}
	
	@Override
	public Linq setDisableSmartSubQueryCriterion() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.disableSmartSubQueryCriterion = true;
		return this;
	}
	
	@Override
	public Linq setDisableBackFillFilter() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.disableBackFillFilter = true;
		return this;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doFilter(Collection list) {		
		if (toEntity) {
			Collection copy = new ArrayList(list.size());
			copy.addAll(list);
			list.clear();
			for (Object entity : copy) {
				try {
					list.add(EntityUtils.toEntity(entity));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		if (filter != null) {
			Iterator<?> iterator = list.iterator();
			while (iterator.hasNext()) {
				Object entity = iterator.next();
				linqContext.setEntity(entity);
				if (!filter.invoke(linqContext)) {
					iterator.remove();
				}
			}
		}
	}

	@Override
	public Linq toEntity() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.toEntity = true;
		return this;
	}

	@Override
	public Linq addParser(CriterionParser criterionParser) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.criterionParsers.add(criterionParser);
		return this;
	}
	
	@Override
	public Linq addSubQueryParser(Class<?>... entityClasses) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (Class<?> entityClass : entityClasses) {
			this.addParser(new SubQueryParser(this, entityClass));
		}
		return this;
	}
	
	@Override
	public Linq addSubQueryParser(Class<?> entityClass, String... foreignKeys) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.addParser(new SubQueryParser(this, entityClass, foreignKeys));
		return this;
	}

	@Override
	public LinqContext getLinqContext() {
		return linqContext;
	}
	
	@Override
	public Linq collect(String ...properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		collect(null, null, null, null, null, properties);
		return this;
	}
	
	@Override
	public Linq collect(Class<?> entityClass) {
		return collect(null, null, null, JpaUtil.getIdName(entityClass), entityClass, JpaUtil.getIdName(domainClass));
	}
	
	@Override
	public Linq collect(Class<?> entityClass, String ...properties) {
		return collect(null, null, null, JpaUtil.getIdName(entityClass), entityClass, properties);
	}
	
	@Override
	public Linq collect(String otherProperty, Class<?> entityClass) {
		return collect(null, null, null, otherProperty, entityClass, JpaUtil.getIdName(domainClass));
	}
	
	@Override
	public Linq collect(String otherProperty, Class<?> entityClass, String ...properties) {
		return collect(null, null, null, otherProperty, entityClass, properties);
	}
	
	@Override
	public Linq collect(Class<?> relationClass, Class<?> entityClass) {
		return collect(relationClass, Introspector.decapitalize(domainClass.getSimpleName()) + "Id",
				Introspector.decapitalize(entityClass.getSimpleName()) + "Id", JpaUtil.getIdName(entityClass),
				entityClass, JpaUtil.getIdName(domainClass));
	}


	@Override
	public Linq collect(Class<?> relationClass, String relationProperty, String relationOtherProperty,
			Class<?> entityClass) {
		return collect(relationClass, relationProperty, relationOtherProperty, JpaUtil.getIdName(entityClass),
				entityClass, JpaUtil.getIdName(domainClass));
	}


	@Override
	public Linq collect(Class<?> relationClass, String relationProperty, String relationOtherProperty,
			String otherProperty, Class<?> entityClass) {
		return collect(relationClass, relationProperty, relationOtherProperty, otherProperty, entityClass,
				JpaUtil.getIdName(domainClass));
	}
	
	@Override
	public Linq collect(Class<?> relationClass, String relationProperty, String relationOtherProperty,
			String otherProperty, Class<?> entityClass, String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		CollectInfo collectInfo = new CollectInfo();
		collectInfo.setEntityClass(entityClass);
		collectInfo.setRelationClass(relationClass);
		collectInfo.setRelationProperty(relationProperty);
		collectInfo.setRelationOtherProperty(relationOtherProperty);
		collectInfo.setOtherProperty(otherProperty);
		collectInfo.setProperties(properties);
		collectInfos.add(collectInfo);
		return this;
	}
	
	@Override
	public Linq collectSelect(Class<?> entityClass, String ...projections) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		projectionMap.put(entityClass, projections);
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	protected void doCollect(Collection list) {
		if (!collectInfos.isEmpty()) {
			initCollectInfos(list);
			buildMetadata();
		}
		doBackfill();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildMetadata() {
		Map<Object, Object> metadata = linqContext.getMetadata();
		for (CollectInfo collectInfo : collectInfos) {
			Set collectSet = collectInfo.getSet();
			Map<Object, Object> relationMap = null;
			List collectList = null;
		
			if (!CollectionUtils.isEmpty(collectSet)) {
				if (collectInfo.getRelationClass() != null) {
					collectList = JpaUtil
						.linq(collectInfo.getRelationClass())
						.aliasToBean()
						.select(collectInfo.getRelationProperty(), collectInfo.getRelationOtherProperty())
						.in(collectInfo.getRelationProperty(), collectSet)
						.list();
					relationMap = JpaUtil.index(collectList, collectInfo.getRelationOtherProperty());
					collectSet = relationMap.keySet();
				}
				for (String property : collectInfo.getProperties()) {
					if (!metadata.containsKey(property)) {
						Class<?> entityClass = collectInfo.getEntityClass();
						if (entityClass != null && !collectSet.isEmpty()) {
							if (metadata.containsKey(entityClass)) {
								metadata.put(property, metadata.get(entityClass));
							} else {
								String otherProperty = collectInfo.getOtherProperty();
								
								Linq linq = JpaUtil.linq(entityClass);
								if (ArrayUtils.isNotEmpty(projectionMap.get(entityClass))) {
									linq.aliasToBean();
									linq.select(projectionMap.get(entityClass));
								}
								linq.in(otherProperty, collectSet);
								List result = linq.list();
								Map<Object, Object> resultMap = JpaUtil.index(result, otherProperty);
								Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();
								if (collectList != null) {
									for (Object obj : collectList) {
										Object key = BeanUtils.getFieldValue(obj, collectInfo.getRelationOtherProperty());
										Object other = resultMap.get(key);
										key = BeanUtils.getFieldValue(obj, collectInfo.getRelationProperty());
										List<Object> list = map.get(key);
										if (list == null) {
											list = new ArrayList<Object>(5);
											map.put(key, list);
										}
										if (other != null) {
											list.add(other);
										}
										
									}
								} else {
									for (Object obj : result) {
										Object key = BeanUtils.getFieldValue(obj, otherProperty);
										List<Object> list = map.get(key);
										if (list == null) {
											list = new ArrayList<Object>(5);
											map.put(key, list);
										}
										list.add(obj);
									}
								}
								
								metadata.put(property, map);
								metadata.put(entityClass, map);
							}
							
						} else {
							metadata.put(property, collectInfo.getSet());
						}
					}
				}
				
			}
			
		}
	}

	private void initCollectInfos(Collection<?> list) {
		for (Object entity : list) {
			BeanMap beanMap = BeanMap.create(entity);
			for (CollectInfo collectInfo : collectInfos) {
				for (String property : collectInfo.getProperties()) {
					Object value = beanMap.get(property);
					if (value != null) {
						collectInfo.add(value);
					}
				}
			}
		}
	}
	
	private void doBackfill() {
		if (!collectInfos.isEmpty() && !disableBackFillFilter) {
			this.filter = new BackfillFilter(this.filter, collectInfos);
		}
		
	}
	
	
	
	
	public LinqImpl(Class<?> domainClass) {
		super(domainClass);
		criteria = cb.createQuery(domainClass);
		root = criteria.from(domainClass);
		resultClass = domainClass;
	}
	
	public LinqImpl(Class<?> domainClass, Class<?> resultClass) {
		super(domainClass);
		if (Tuple.class.isAssignableFrom(resultClass)) {
			criteria = cb.createTupleQuery();
		} else {
			criteria = cb.createQuery(resultClass);
		}
		root = criteria.from(domainClass);
		this.resultClass = resultClass;
	}
	
	public LinqImpl(Linq parent, Class<?> domainClass) {
		super(parent, domainClass);
	}
	
	@Override
	public Linq distinct() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		distinct = true;
		return this;
	}
	
	@Override
	public Linq groupBy(String... grouping) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		List<Expression<?>> expressions = new ArrayList<Expression<?>>();
		for (String property : grouping) {
			expressions.add(root.get(property));
		}
		if (sq != null) {
			sq.groupBy(expressions);
		} else {
			criteria.groupBy(expressions);
		}
		return this;
	}
	
	@Override
	public Linq desc(String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (String property : properties) {
			orders.add(cb.desc(root.get(property)));
		}
		return this;
	}
	
	@Override
	public Linq desc(Expression<?>... expressions) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (Expression<?> expression : expressions) {
			orders.add(cb.desc(expression));
		}
		return this;
	}
	
	@Override
	public Linq asc(String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (String property : properties) {
			orders.add(cb.asc(root.get(property)));
		}
		return this;
	}
	
	@Override
	public Linq asc(Expression<?>... expressions) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (Expression<?> expression : expressions) {
			orders.add(cb.asc(expression));
		}
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T findOne() {
		if (parent != null) {
			beforeExecute(sq);
			return (T) parent.findOne();
		}
		beforeExecute(criteria);
		List<T> list = transform(em.createQuery(criteria), true);
		return list.get(0);
	}

	@Override
	public <T> List<T> list() {
		if (parent != null) {
			beforeExecute(sq);
			return parent.list();
		}
		beforeExecute(criteria);
		List<T> list = transform(em.createQuery(criteria), false);
		return list;
	}
	
	@Override
	public <T> void paging(Page<T> page) {
		if (parent != null) {
			beforeExecute(sq);
			parent.paging(page);
			return;
		}
		List<T> list = Collections.<T> emptyList();
		Long total = 0L;
		if (page == null) {
			list = list();
			total = (long) list.size();
		} else {
			beforeExecute(criteria);
			TypedQuery<?> query = em.createQuery(criteria);
			
			query.setFirstResult((page.getPageNo() - 1)*page.getPageSize());
			query.setMaxResults(page.getPageSize());

			total = JpaUtil.count(criteria);
			if (total > (page.getPageNo() - 1)*page.getPageSize()) {
				list = transform(query, false);
			}
		}
		page.setEntities(list);
		page.setEntityCount(total.intValue());
	}
	
	@Override
	public <T> org.springframework.data.domain.Page<T> paging(Pageable pageable) {
		if (parent != null) {
			beforeExecute(sq);
			return parent.paging(pageable);
		}
		List<T> list;
		if (pageable == null) {
			list = list();
			return new PageImpl<T>(list);
		} else {
			Sort sort = pageable.getSort();
			orders.addAll(QueryUtils.toOrders(sort, root, cb));
			beforeExecute(criteria);
			TypedQuery<?> query = em.createQuery(criteria);
			Long offset = pageable.getOffset();
			query.setFirstResult(offset.intValue());
			query.setMaxResults(pageable.getPageSize());

			Long total = JpaUtil.count(criteria);
			List<T> content = Collections.<T> emptyList();
			if (total > pageable.getOffset()) {
				content = transform(query, false);
			}

			return new PageImpl<T>(content, pageable, total);
		}
	}
	
	@Override
	public Long count() {
		if (parent != null) {
			beforeExecute(sq);
			return parent.count();
		}
		return executeCountQuery(getCountQuery());
	}
	
	@Override
	public boolean exists() {
		if (parent != null) {
			beforeExecute(sq);
			return parent.exists();
		}
		return count() > 0;
	}
	
	protected Long executeCountQuery(TypedQuery<Long> query) {

		Assert.notNull(query, "query can not be null.");

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}
	
	protected TypedQuery<Long> getCountQuery() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		criteria.getRoots().add(root);
		beforeExecute(criteria);
		criteria.getOrderList().clear();
		if (distinct) {
			criteria.select(cb.countDistinct(root));
		} else {
			criteria.select(cb.count(root));
		}
		return em.createQuery(criteria);
	}
	
	protected void applyPredicateToCriteria(AbstractQuery<?> query) {

		Predicate predicate = parsePredicate(junction);
		if (predicate != null) {
			query.where(predicate);
		}
		
		predicate = parsePredicate(having);
		if (predicate != null) {
			query.having(predicate);
		}
		
		if (query instanceof CriteriaQuery) {
			if (!CollectionUtils.isEmpty(orders)) {
				((CriteriaQuery<?>) query).orderBy(orders);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> List<T> transform(TypedQuery<?> query, boolean single) {
		List<T> result;
		if (resultTransformer != null) {
			List tuples;
			if (single) {
				tuples = new ArrayList(1);
				tuples.add(query.getSingleResult());
			} else {
				tuples = query.getResultList();
			}
			result = new ArrayList<T>(tuples.size());
			String[] aliases = this.aliases.toArray(new String[this.aliases.size()]);
			for (Object tuple : tuples) {
				if (tuple != null) {
					if (tuple.getClass().isArray()) {
						result.add((T) resultTransformer.transformTuple((Object[]) tuple, aliases));
					} else {
						result.add((T) resultTransformer.transformTuple(new Object[]{ tuple }, aliases));
					}
				}
			}
		} else {
			if (single) {
				result = new ArrayList<T>(1);
				result.add((T) query.getSingleResult());
			} else {
				result = (List<T>) query.getResultList();
			}
		}
			
		afterExecute(result);
		return result;
		
	}

	@Override
	public Linq createChild(Class<?> domainClass) {
		return new LinqImpl(this, domainClass);
	}

	@Override
	public Linq aliasToBean() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createQuery(Object[].class);
		root = criteria.from(domainClass);
		resultTransformer = Transformers.aliasToBean(domainClass);
		return this;
	}
	
	@Override
	public Linq aliasToBean(Class<?> resultClass) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createQuery(Object[].class);
		root = criteria.from(domainClass);
		this.resultClass = resultClass;
		resultTransformer = Transformers.aliasToBean(resultClass);
		return this;
	}

	@Override
	public Linq aliasToMap() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createQuery(Object[].class);
		root = criteria.from(domainClass);
		this.resultClass = Map.class;
		resultTransformer = Transformers.ALIAS_TO_MAP;
		return this;
	}

	@Override
	public Linq aliasToTuple() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createTupleQuery();
		root = criteria.from(domainClass);
		resultClass = Tuple.class;
		return this;
	}
	
	@Override
	public <T> List<T> list(Pageable pageable) {
		if (parent != null) {
			applyPredicateToCriteria(sq);
			return parent.list(pageable);
		}
		if (pageable == null) {
			return list();
		} else {
			Sort sort = pageable.getSort();
			orders.addAll(QueryUtils.toOrders(sort, root, cb));
			applyPredicateToCriteria(criteria);
			TypedQuery<?> query = em.createQuery(criteria);
			
			Long offset = pageable.getOffset();
			query.setFirstResult(offset.intValue());
			query.setMaxResults(pageable.getPageSize());

			return transform(query, false);
		}
	}

	@Override
	public <T> List<T> list(int page, int size) {
		if (parent != null) {
			applyPredicateToCriteria(sq);
			return parent.list(page, size);
		}
		applyPredicateToCriteria(criteria);
		TypedQuery<?> query = em.createQuery(criteria);
		
		query.setFirstResult(page*size);
		query.setMaxResults(size);

		return transform(query, false);
	}


	@Override
	public <T> List<T> list(Page<T> page) {
		List<T> result = list(page.getPageNo() - 1, page.getPageSize());
		page.setEntities(result);
		if (result.size() == page.getPageSize()) {
			page.setEntityCount(Integer.MAX_VALUE);
		} else {
			page.setEntityCount(( page.getPageNo() - 1 ) * page.getPageSize() + result.size());
		}
		return result;
	}


}
