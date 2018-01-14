package com.android.group0674.onlinestore.Model.users;

import android.content.Context;

import java.io.Serializable;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.security.PasswordHelpers;

/**
 * This is the abstract class that provides information about the user.
 * 
 * @author dharmik
 *
 */
public abstract class User implements Serializable {
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 7169363111303228996L;
  private int id;
  private int age;
  private int roleId;
  private String name;
  private String address;
  private String encryptedPassword;
  private UserPrinter printer;
  private boolean authenticated = false;
  private Context context;

  /**
   * The user constructor.
   * 
   * @param id - the user id.
   * @param name - the user name.
   * @param age - the user age.
   * @param address - the user password.
   */
  public User(int id, String name, int age, String address, Context context) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.context = context;
  }

  /**
   * The user constructor with authenticated parameter.
   * 
   * @param id - the user id.
   * @param name - the user name.
   * @param age - the user age.
   * @param address - the user password.
   * @param authenticated - whether the user is authenticated or not.
   */
  public User(int id, String name, int age, String address, boolean authenticated) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.authenticated = authenticated;
  }

  /**
   * This method will return whether the user is authenticated or not.
   * 
   * @return - true or false.
   */
  public boolean isAuthenticated() {
    return authenticated;
  }

  /**
   * This method will set the authenticated status of the user.
   * 
   * @param authenticated - true or false status.
   */
  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  /**
   * This method will return the id of the user.
   * 
   * @return - the id of user.
   */
  public int getId() {
    return this.id;
  }

  /**
   * This method will set the id of the user.
   * 
   * @param id - the id to be set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * This method will return the age of the user.
   * 
   * @return - the age.
   */
  public int getAge() {
    return this.age;
  }

  /**
   * This method will set the age of the user.
   * 
   * @param age - the age to be set.
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * This method will return the name of the user.
   * 
   * @return - the name of the user.
   */
  public String getName() {
    return this.name;
  }

  /**
   * This method will set the name of the user.
   * 
   * @param name - the name to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * This method will return the role id of the user.
   * 
   * @return - the role id of the user.
   */
  public int getRoleId() {
    return this.roleId;
  }

  /**
   * This method will set the role id of the user.
   * 
   * @param roleId - the role id of the user.
   */
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  public String getEncryptedPassword() {
    return this.encryptedPassword;
  }

  public void setEncryptedPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  /**
   * This method will return the address.
   * 
   * @return - the address.
   */
  public String getAddress() {
    return this.address;
  }


  /**
   * This method will return whether the authentication of the user is successful.
   * 
   * @param password - the password to check with the database password.
   * @return - true or false.
   */
  public final boolean authenticate(String password) {
    // get the hashed version of this password
    String hashedPassword = DatabaseSelectHelper.getPassword(this.id, context);
    boolean result = PasswordHelpers.comparePassword(hashedPassword, password);
    if (result) {
      System.out.println("Access granted.\n");
    } else {
      System.out.println("Access denied.\n");
    }
    return result;

  }

  /**
   * Prints the details of the user.
   */
  public void displayUser() {
    this.printer = new UserPrinter();
    this.printer.displayItems(this);
  }

}
