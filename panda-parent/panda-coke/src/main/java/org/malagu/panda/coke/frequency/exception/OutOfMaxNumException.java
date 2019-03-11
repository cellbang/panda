package org.malagu.panda.coke.frequency.exception;

/**
 * 超出最大调用次数异常
 * @author sr on 2019-03-11 
 */
public class OutOfMaxNumException extends RuntimeException{

  /**  **/
  private static final long serialVersionUID = 1L;

  public OutOfMaxNumException() {
    super();
  }

  public OutOfMaxNumException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public OutOfMaxNumException(String message, Throwable cause) {
    super(message, cause);
  }

  public OutOfMaxNumException(String message) {
    super(message);
  }

  public OutOfMaxNumException(Throwable cause) {
    super(cause);
  }
  
}
