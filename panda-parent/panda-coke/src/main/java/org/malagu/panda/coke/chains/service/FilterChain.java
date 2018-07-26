package org.malagu.panda.coke.chains.service;

import java.util.ArrayList;
import java.util.List;

public class FilterChain<K, V> implements Filter<K, V> {
  List<Filter<K, V>> filters = new ArrayList<Filter<K, V>>();
  int index = 0;

  public FilterChain<K, V> addFilter(Filter<K, V> f) {
    filters.add(f);
    return this;
  }

  @Override
  public void doFilter(K request, V response, FilterChain<K, V> chain) {
    if (index == filters.size()) {
      return;
    }

    Filter<K, V> f = filters.get(index);
    index++;
    f.doFilter(request, response, chain);
  }
}
