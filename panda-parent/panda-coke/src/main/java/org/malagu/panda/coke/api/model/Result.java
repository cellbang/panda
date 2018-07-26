package org.malagu.panda.coke.api.model;

public class Result {
  public static final String Success = "S";
  public static final String Fail = "F";
  public static final String Unknown = "U";

  public static boolean isSuccess(String status) {
    return Success.equals(status);
  }

  private ResultInfo resultInfo;
  private Object data;

  public static Result success() {
    Result result = new Result();
    result.resultInfo = ResultInfo.build("S", "0000", null);
    return result;
  }

  public static Result fail(String code, String msg) {
    Result result = new Result();
    result.resultInfo = ResultInfo.build("F", code, msg);
    return result;
  }

  public ResultInfo getResultInfo() {
    return resultInfo;
  }

  public void setResultInfo(ResultInfo resultInfo) {
    this.resultInfo = resultInfo;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}


class ResultInfo {

  private String resultStatus;
  private String resultCode;
  private String resultMsg;

  public static ResultInfo success() {
    return build("S", "0000", null);
  }

  public static ResultInfo fail(String code, String msg) {
    return build("F", code, msg);
  }

  public static ResultInfo unknown(String code, String msg) {
    return build("U", code, msg);
  }

  public static ResultInfo build(String status, String code, String msg) {
    ResultInfo resultInfo = new ResultInfo();
    resultInfo.resultStatus = status;
    resultInfo.resultCode = code;
    resultInfo.resultMsg = msg;
    return resultInfo;
  }

  public String getResultStatus() {
    return resultStatus;
  }

  public void setResultStatus(String resultStatus) {
    this.resultStatus = resultStatus;
  }

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

}
