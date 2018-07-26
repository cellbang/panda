package org.malagu.panda.coke.pinyin.view;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.malagu.panda.coke.entity.PinyinConverter;
import org.malagu.panda.coke.model.Pinyin;
import org.malagu.panda.coke.querysupporter.service.impl.SynonymServiceImpl;
import org.malagu.panda.coke.utility.PinyinUtils;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

@Service("coke.pinyinMaintain")
public class PinyinMaintain {

	@DataProvider
	public Collection<Pinyin> loadAllPinyin(String hanzi) {
		return PinyinUtils.toPinyin(hanzi);
	}

	@DataProvider
	public Collection<PinyinConverter> loadPinyinConverters() {
		Collection<PinyinConverter> pinyinEntityList = new ArrayList<PinyinConverter>();
		Collection<Entry<Class<?>, Map<String, Collection<String>>>> clazzList = SynonymServiceImpl.getPinyinmap()
				.getData().entrySet();
		for (Entry<Class<?>, Map<String, Collection<String>>> clazz : clazzList) {
			PinyinConverter pinyinConverter = new PinyinConverter();
			pinyinConverter.setClazz(clazz.getKey().getName());

			Map<String, Collection<String>> pm = clazz.getValue();
			Set<Entry<String, Collection<String>>> entries = pm.entrySet();
			if (!entries.isEmpty()) {
				Entry<String, Collection<String>> entry = entries.iterator().next();
				pinyinConverter.setProperty(entry.getKey());

				Collection<String> p = entry.getValue();
				for (String string : p) {
					if (string.toLowerCase().contains("quan")) {
						pinyinConverter.setQuanpinProperty(string);
					} else if (string.toLowerCase().contains("jian")) {
						pinyinConverter.setJianpinProperty(string);
					}
				}
				pinyinEntityList.add(pinyinConverter);
			}

		}
		return pinyinEntityList;
	}

	@Expose
	@Transactional
	public void batchConvert(Map<String, Object> parameter) {
		String clazzName = (String) parameter.get("clazz");
		String property = (String) parameter.get("property");
		String quanProperty = (String) parameter.get("quanpinProperty");
		String jianProperty = (String) parameter.get("jianpinProperty");
		Integer batchSize = (Integer) parameter.get("batchSize");

		if (StringUtils.isEmpty(property)) {
			property = "name";
		}

		if (batchSize == null) {
			batchSize = 200;
		}
		Session session = JpaUtil.getEntityManager().unwrap(Session.class);

		Collection<?> list;

		Long rowCount = null;
		Long currentIndex = 0L;
		do {
			DetachedCriteria dc = createDetachedCriteria(clazzName);
			dc.add(Restrictions.or(Restrictions.isNull(quanProperty), Restrictions.isNull(jianProperty)));
			dc.add(Restrictions.isNotNull(property));
			Criteria criteria = dc.getExecutableCriteria(session);

			if (rowCount == null) {
				rowCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
			}
			criteria.setProjection(null);
			criteria.setFirstResult(0);
			criteria.setMaxResults(batchSize);
			list = criteria.list();

			Transaction transaction = session.beginTransaction();
			for (Object object : list) {
				try {
					String value = BeanUtils.getProperty(object, property);
					if (StringUtils.isEmpty(value)) {
						continue;
					}
					String quanValue = PinyinHelper.convertToPinyinString(value, "", PinyinFormat.WITHOUT_TONE);
					String jianValue = PinyinHelper.getShortPinyin(value);

					quanValue = extractCharDigits(quanValue);
					jianValue = extractCharDigits(jianValue);

					BeanUtils.setProperty(object, quanProperty, quanValue);
					BeanUtils.setProperty(object, jianProperty, jianValue);

					session.update(object);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				} finally {
					currentIndex++;
				}
			}
			transaction.commit();
			session.flush();
			session.clear();
		} while (!list.isEmpty() && currentIndex < rowCount);

	}

	public static String extractCharDigits(String content) {
		StringBuilder builder = new StringBuilder(content.length());
		for (int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	@Expose
	@Transactional
	public Set<Entry<String, Integer>> prewPinyin(Map<String, Object> parameter)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String clazzName = (String) parameter.get("clazz");
		String property = (String) parameter.get("property");
		String quanProperty = (String) parameter.get("quanpinProperty");
		String jianProperty = (String) parameter.get("jianpinProperty");
		Integer batchSize = (Integer) parameter.get("batchSize");

		if (StringUtils.isEmpty(property)) {
			property = "name";
		}

		if (batchSize == null) {
			batchSize = 200;
		}
		Session session = JpaUtil.getEntityManager().unwrap(Session.class);

		Collection<?> list;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		do {
			DetachedCriteria dc = createDetachedCriteria(clazzName);
			dc.add(Restrictions.or(Restrictions.isNull(quanProperty), Restrictions.isNull(jianProperty)));
			dc.add(Restrictions.isNotNull(property));
			Criteria criteria = dc.getExecutableCriteria(session);
			criteria.setFirstResult(0);
			criteria.setMaxResults(batchSize);
			list = criteria.list();

			for (Object object : list) {
				String value = BeanUtils.getProperty(object, property);
				if (value != null && value.length() > 1) {
					addSentence(wordMap, value);
				}
			}
			session.clear();
		} while (!list.isEmpty());

		wordMap = sortByValue(wordMap);
		return wordMap.entrySet();
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void addSentence(Map<String, Integer> wordMap, String sentence) {
		if (sentence != null && sentence.length() > 1) {
			for (int i = 0; i <= sentence.length(); i++) {
				for (int j = i + 2; j <= sentence.length(); j++) {
					addWord(wordMap, sentence.substring(i, j));
				}
			}
		}
	}

	public static void addWord(Map<String, Integer> wordMap, String word) {
		Integer count = wordMap.get(word);
		if (count == null) {
			count = 0;
		}
		count++;
		wordMap.put(word, count);
	}

	public static void main(String[] args) {
		Map<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
		addSentence(wordMap, "大家好我是(小不) . 哥, .哥");

		System.out.println(extractCharDigits("a(b,c"));
		System.out.println(wordMap);
	}

	public DetachedCriteria createDetachedCriteria(String name) {
		DetachedCriteria dc = null;
		if (name.contains(".")) {
			Class<?> clazz;
			try {
				clazz = Class.forName(name);
				dc = DetachedCriteria.forClass(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			dc = DetachedCriteria.forEntityName(name);
		}
		return dc;
	}

}
