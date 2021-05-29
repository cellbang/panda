package org.malagu.panda.coke.querysupporter.service;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.malagu.panda.coke.querysupporter.model.JunctionAndParam;
import org.malagu.panda.coke.querysupporter.model.JunctionQueryParam;
import org.malagu.panda.coke.querysupporter.model.JunctionOrParam;

public class QueryParam {
  Deque<Boolean> ifStack = new LinkedList<Boolean>();
  Deque<JunctionQueryParam> queryParamStack = new LinkedList<>();
  int count = 0;

  private QueryParam(Map<String, Object> parameter) {
    queryParamStack.push(new JunctionAndParam(parameter));
  }

  public static QueryParam wrap() {
    return new QueryParam(new HashMap<String, Object>());
  }

  public static QueryParam wrap(Map<String, Object> parameter) {
    return new QueryParam(parameter);
  }

  boolean is(Object target) {
    boolean result;
    if (target == null) {
      result = false;
    } else if (Boolean.TRUE.equals(target)) {
      result = (Boolean) target;
    } else if (target instanceof Collection) {
      result = !((Collection<?>) target).isEmpty();
    } else if (target.getClass().isArray()) {
      result = Array.getLength(target) > 0;
    } else {
      result = !target.toString().isEmpty();
    }
    return result;
  }

  public QueryParam addIfNot(Object target) {
    ifStack.push(!is(target));
    return this;
  }

  public QueryParam addIf(Object target) {
    ifStack.push(is(target));
    return this;
  }

  public QueryParam endIf() {
    ifStack.pop();
    return this;
  }

  boolean shouldContinue() {
    for (Boolean value : ifStack) {
      if (!Boolean.TRUE.equals(value)) {
        return false;
      }
    }
    return true;
  }

  public QueryParam or() {
    if (!shouldContinue()) {
      return this;
    }
    JunctionOrParam or = JunctionOrParam.create(count++);
    queryParamStack.peek().add(or.getName(), or);
    queryParamStack.push(or);
    return this;
  }

  public QueryParam and() {
    if (!shouldContinue()) {
      return this;
    }
    JunctionAndParam and = JunctionAndParam.create(count++);
    queryParamStack.peek().add(and.getName(), and);
    queryParamStack.push(and);
    return this;
  }

  public QueryParam endOr() {
    if (!shouldContinue()) {
      return this;
    }
    queryParamStack.pop();
    return this;
  }

  public QueryParam endAnd() {
    if (!shouldContinue()) {
      return this;
    }

    queryParamStack.pop();
    return this;
  }

  public QueryParam add(String property, Object value) {
    if (!shouldContinue()) {
      return this;
    }
    queryParamStack.peek().add(property, value);
    return this;
  }

  public QueryParam remove(String property) {
    if (!shouldContinue()) {
      return this;
    }
    queryParamStack.peek().remove(property);
    return this;
  }

  public Map<String, Object> toQueryParams() {
    return queryParamStack.getLast().getQueryParameter();
  }

}
