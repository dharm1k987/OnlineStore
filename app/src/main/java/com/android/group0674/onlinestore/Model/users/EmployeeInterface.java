package com.android.group0674.onlinestore.Model.users;

import android.content.Context;

import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseUpdateHelper;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseInsertException;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseMissingTableException;
import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;
import com.android.group0674.onlinestore.Model.inventory.Inventory;
import com.android.group0674.onlinestore.Model.inventory.Item;
import java.util.HashMap;

/**
 * The class that the employee will be interacting with, where they can fiddle with the inventory.
 * 
 * @author dharmik
 *
 */
public class EmployeeInterface
    implements InventoryRestockable, EmployeeCreatable, CustomerCreatable {

  private Employee currentEmployee = null;
  private Inventory inventory;
  private Context context;

  /**
   * The constructor, will first attempt to authenticate employee if not done so already.
   * 
   * @param currentEmployee - the employee to be authenticated or already authenticated.
   * @param inventory - the inventory.
   */
  public EmployeeInterface(Employee currentEmployee, Inventory inventory, Context context) {
    // CHECK AUTHENTICATION
    this.context = context;
    if (currentEmployee.isAuthenticated()) {
      this.currentEmployee = currentEmployee;
      this.inventory = inventory;
    }
  }

  /**
   * This constructor will simply set the inventory.
   * 
   * @param inventory - the inventory to be set.
   */
  public EmployeeInterface(Inventory inventory) {
    this.inventory = inventory;
  }

  /**
   * This method will set the current employee.
   * 
   * @param currentEmployee - the current employee.
   */
  public void setCurrentEmployee(Employee currentEmployee) {
    if (currentEmployee.isAuthenticated()) {
      // if they are authenticated, set it, otherwise we can't
      this.currentEmployee = currentEmployee;
    } else {
      System.out.println("Not authenticated");
    }
  }

  /**
   * This method checks to see if the employee interface has a current employee.
   * 
   * @return - true or false depending on whether we have or not.
   */
  public boolean hasCurrentEmployee() {
    return this.currentEmployee != null;
  }

  /**
   * This method takes in an item and quantity, and restocks the inventory accordingly.
   * 
   * @param item - the item we want to restock.
   * @param quantity - the quantity we want to restock it to,
   * @return - true if successful, false otherwise (invalid item or quantity)
   */
  @Override
  public boolean restockInventory(Item item, int quantity) {
    if (quantity < 0 || item == null) {
      return false;
    }
    // if valid, we want to put this information in the inventory hashmap
    HashMap<Item, Integer> itemMap = inventory.getItemMap();
    boolean valid = DatabaseUpdateHelper.updateInventoryQuantity(quantity, item.getId(), context);
    if (valid) {
      itemMap.put(item, quantity);
      return true;
    }
    // if we reach here that means one or more of the inputs were invalid
    return false;
  }

  /**
   * This method will create a new customer from the given paramaters.
   * 
   * @param name - the name of the customer.
   * @param age - the age of the customer.
   * @param address - the address of the customer.
   * @param password - the password of the customer.
   * @return - customerId if successful, -1 in error (invalid parameters).
   * @throws InvalidArgumentException - if the paramaters do not match the specifications.
   */
  @Override
  public int createCustomer(String name, int age, String address, String password)
      throws InvalidArgumentException {
    int userId;
    // create a new user in the database and set userId equal to it
    try {
      if ((userId = DatabaseInsertHelper.insertNewUser(name, age, address, password, context)) < 0) {
        // throw an exception as input is not valid
        throw new InvalidArgumentException("Invalid inputs");
      } else {
        // put them as customer (3 is the id of customer)
        DatabaseInsertHelper.insertUserRole(userId,
            DatabaseSelectHelper.getRoleIdByName("CUSTOMER", context), context);
      }
    } catch (Exception e) {
      // throw an exception as input is not valid
      throw new InvalidArgumentException("Invalid inputs");
    }
    return userId;
  }

  /**
   * This method will create a new employee from the given paramaters.
   * 
   * @param name - the name of the employee.
   * @param age - the age of the employee.
   * @param address - the address of the employee.
   * @param password - the password of the employee.
   * @return - the userId if successful, -1 otherwise (invalid paramaters).
   * @throws InvalidArgumentException - if the paramaters do not match the specifications.
   */
  @Override
  public int createEmployee(String name, int age, String address, String password)
      throws InvalidArgumentException {
    int userId;
    // create a new user in the database and set userId equal to it
    try {
      if ((userId = DatabaseInsertHelper.insertNewUser(name, age, address, password, context)) < 0) {
        // throw an exception as input is not valid
        throw new InvalidArgumentException("Invalid inputs");
      } else {
        // put them as employee (2 is id of employee)
        DatabaseInsertHelper.insertUserRole(userId,
            DatabaseSelectHelper.getRoleIdByName("EMPLOYEE", context), context);
      }
    } catch (Exception e) {
      // throw an exception as input is not valid
      throw new InvalidArgumentException("Invalid inputs");
    }
    return userId;
  }

  /**
   * This method will create an account for a given customer.
   * 
   * @param customerAccount - the customer object we want to create an account of.
   * @throws DatabaseInsertException - if the account could not be created.
   */
  public void createAccount(Customer customerAccount) throws DatabaseInsertException {
    // insert the account which is active by default
    int accountId = DatabaseInsertHelper.insertAccount(customerAccount.getId(), true, context);
    // create the account object to set to the customer
    Account account = new Account(accountId, customerAccount.getId(), context);
    ((Customer) customerAccount).setAccount(account);
    account.setAccountStatus(true);

    // if valid account id
    if (accountId != -1) {
      System.out.println("The account id is " + accountId);
      System.out.println("Account has been created\n");
    } else {
      System.out.println("Error creating account");
    }
  }

}
