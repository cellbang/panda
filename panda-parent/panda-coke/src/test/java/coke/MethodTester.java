package coke;

import java.util.HashMap;
import java.util.Map;

import org.malagu.panda.coke.utility.MethodUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MethodTester {

  public static void main(String[] args) throws Exception {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("name", "Bing");
    parameters.put("address", "Shanghai");
    parameters.put("age", 28);

    Map<String, Object> subUserInfo = new HashMap<String, Object>();
    subUserInfo.put("name", "xiaobu");
    subUserInfo.put("address", "Zhengzhou");
    subUserInfo.put("age", 25);

    parameters.put("userInfo", subUserInfo);

    // MethodUtils.invokeMethod(new MethodTester(), "test", parameters);

    String sb = "{" +
        "	name: \"Bing\"," +
        "	address: \"i@xobo.org\"," +
        "	age: 30," +
        "	userInfo: {" +
        "		name:\"user\"," +
        "		birthday: \"19870928\"" +
        "	}" +
        "}";
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    JsonNode jp = mapper.readTree(sb);
    // jp = jp.get("userInfo");
    System.out.println(jp);
    MethodUtils.invokeMethod(new MethodTester(), "test", jp);

  }

  public void test(String name, String address, int age, UserInfo userInfo) {
    System.out.println("name: " + name + "\taddress: " + address + "\t age: " + age);
    System.out.println(userInfo);
  }

}
