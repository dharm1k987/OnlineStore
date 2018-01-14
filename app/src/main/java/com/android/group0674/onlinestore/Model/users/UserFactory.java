package com.android.group0674.onlinestore.Model.users;

import android.content.Context;
import android.util.Log;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;

/**
 * This class serves as the factory that creates User instances.
 * 
 * @author shahdha2
 *
 */
public class UserFactory {
  /**
   * This method will create a specific type of user based on the given info.
   * 
   * @param userId - the userId of the user.
   * @param name - the name of the user.
   * @param age - the age of the user.
   * @param address - the address of the user.
   * @return - an Admin, Employee, Customer, or null.
   */
  public static User getUser(int userId, String name, int age, String address, Context context) {
    // set it initially as null
    User user = null;
    // figure out the role based on the user id
    int role = DatabaseSelectHelper.getUserRoleId(userId, context);
    Log.d("the role id of the person is ", Integer.toString(role));
    if (role == DatabaseSelectHelper.getRoleIdByName("EMPLOYEE", context)) {
      // EMPLOYEE
      user = new Employee(userId, name, age, address, context);
      user.setRoleId(DatabaseSelectHelper.getRoleIdByName("EMPLOYEE", context));
      user.setEncryptedPassword(DatabaseSelectHelper.getPassword(userId, context));
      // this means customer
    } else if (role == DatabaseSelectHelper.getRoleIdByName("CUSTOMER", context)) {
      // CUSTOMER
      user = new Customer(userId, name, age, address, context);
      user.setRoleId(DatabaseSelectHelper.getRoleIdByName("CUSTOMER", context));
      user.setEncryptedPassword(DatabaseSelectHelper.getPassword(userId, context));
      int accountId = -1;
      if ((accountId = DatabaseSelectHelper.checkIfAccountExists(userId, context)) != -1) {
        // account exists, add to customer
        // get account from database
        Account account = DatabaseSelectHelper.getAccountDetails(accountId, userId, context);
        ((Customer) user).setAccount(account);
      }

    }
    // if it was none, user will still be null
    return user;
  }
}
