package com.android.group0674.onlinestore.Model.users;

import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;

/**
 * This interface allows the employee to create another employee.
 * 
 * @author shahdha2
 *
 */
public interface EmployeeCreatable {
  public int createEmployee(String name, int age, String address, String password)
      throws InvalidArgumentException;
}
