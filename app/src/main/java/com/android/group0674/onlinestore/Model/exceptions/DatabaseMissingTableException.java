package com.android.group0674.onlinestore.Model.exceptions;

public class DatabaseMissingTableException extends Exception {

  /**
   * serialID for missing data exceptions exceptions.
   */
  private static final long serialVersionUID = 7933595261813393004L;

  /**
   * The constructor of the exception.
   * 
   * @param message - the message to be printed in e.getMessage().
   */
  public DatabaseMissingTableException(String message) {
    super(message);
  }

}
