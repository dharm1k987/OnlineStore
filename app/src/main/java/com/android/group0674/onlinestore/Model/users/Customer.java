package com.android.group0674.onlinestore.Model.users;

import android.content.Context;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;

/**
 * The customer class which is a type of user, which can buy items.
 * 
 * @author dharmik
 *
 */
public class Customer extends User {
  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private boolean authenticated;
  private Account account = null;
  private Context context;

  /**
   * This is the customer for the user the employee class.
   * 
   * @param id - the userId of the customer.
   * @param name - the name of the customer.
   * @param age - the age of the customer.
   * @param address - the address of the customer.
   */
  public Customer(int id, String name, int age, String address, Context context) {
    // call the user superclass
    super(id, name, age, address, context);
    // set the role id
    this.context = context;
    this.roleId = DatabaseSelectHelper.getRoleIdByName("CUSTOMER", context);

  }

  /**
   * This is the constructor for the user the customer class (with authentication option).
   * 
   * @param id - the userId of the customer.
   * @param name - the name of the customer.
   * @param age - the age of the customer.
   * @param address - the address of the customer.
   * @param authenticated - whether or not the customer is authenticated.
   */
  public Customer(int id, String name, int age, String address, boolean authenticated) {
    super(id, name, age, address, authenticated);
    // set the role id
    this.roleId = DatabaseSelectHelper.getRoleIdByName("CUSTOMER", context);

  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Account getAccount() {
    return this.account;
  }

}
