package coke;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.bstek.dorado.util.Assert;

public class TestMain {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("l1", 100L);
    map.put("l2", "100L");
    map.put("l3", "100");
    map.put("l3", "abc");
    System.out.println(MapUtils.getLong(map, "l1"));
    System.out.println(MapUtils.getLong(map, "l2"));
    System.out.println(MapUtils.getLong(map, "l3"));
    System.out.println(MapUtils.getLong(map, "l4"));
    System.out.println(MapUtils.getLong(null, "l4"));
    Assert.notNull(MapUtils.getLong(null, "l4"), "Object cannot be null");
  }
}
