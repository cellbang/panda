package coke;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserInfo {
  private String name;
  private String address;
  private int age;
  @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
  private Date birthday;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "UserInfo [name=" + name + ", address=" + address + ", age=" + age + ", birthday="
        + birthday + "]";
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }
}
