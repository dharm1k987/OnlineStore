package com.android.group0674.onlinestore.Model.exceptions;

public class InvalidArgumentException extends Exception {

  /**
   * serialID for invalid arguments exceptions.
   */
  private static final long serialVersionUID = -1508502852321391529L;

  /**
   * The constructor of the exception.
   * 
   * @param message - the message to be printed in e.getMessage().
   */
  public InvalidArgumentException(String message) {
    super(message);
  }

}
