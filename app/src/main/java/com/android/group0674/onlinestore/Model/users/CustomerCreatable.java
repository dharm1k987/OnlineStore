package com.android.group0674.onlinestore.Model.users;

import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;

/**
 * This interface allows an employee to create a customer.
 * 
 * @author shahdha2
 *
 */
public interface CustomerCreatable {
  public int createCustomer(String name, int age, String address, String password)
      throws InvalidArgumentException;

}
