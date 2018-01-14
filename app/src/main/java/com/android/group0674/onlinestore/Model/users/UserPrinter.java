package com.android.group0674.onlinestore.Model.users;


/**
 * Prints the user details in a specific format including the id, name, age, address and roleId of
 * the User.
 * 
 * @author patelv73
 *
 */
public class UserPrinter implements UserPrintable {

  @Override
  public void displayItems(User user) {
    System.out.println("User Id is : " + user.getId());
    System.out.println("User Name is : " + user.getName());
    System.out.println("User Age is : " + user.getAge());
    System.out.println("User Role Id is : " + user.getRoleId());

  }
}
