package com.android.group0674.onlinestore.Model.users;

import android.content.Context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.inventory.ItemComparable;
import com.android.group0674.onlinestore.Model.store.Sale;
import com.android.group0674.onlinestore.Model.store.SalesLog;

/**
 * The employee class which is a type of user, which can change the inventory.
 * 
 * @author dharmik
 *
 */
public class Employee extends User implements ItemComparable {
  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private boolean authenticated;
  private Context context;

  /**
   * This is the constructor for the user the employee class.
   * 
   * @param id - the userId of the employee.
   * @param name - the name of the employee.
   * @param age - the age of the employee.
   * @param address - the address of the employee.
   */
  public Employee(int id, String name, int age, String address, Context context) {
    // call the user superclass
    super(id, name, age, address, context);
    // set the role id
    this.context = context;
    this.roleId = DatabaseSelectHelper.getRoleIdByName("EMPLOYEE", context);

  }

  /**
   * This is the constructor for the user the employee class (with authentication option).
   * 
   * @param id - the userId of the employee.
   * @param name - the name of the employee.
   * @param age - the age of the employee.
   * @param address - the address of the employee.
   * @param authenticated - whether or not the employee is authenticated.
   */
  public Employee(int id, String name, int age, String address, boolean authenticated) {
    // call the user superclass
    super(id, name, age, address, authenticated);
    // set the role id
    this.roleId = DatabaseSelectHelper.getRoleIdByName("EMPLOYEE", context);

  }

  /**
   * This method will print a list of past sales.
   */
  public void viewBooks() {
    // get salesLog from the database;
    SalesLog salesLog = DatabaseSelectHelper.getSales(context);
    // get the itemized salesLog
    DatabaseSelectHelper.getItemizedSales(salesLog, context);

    // create a totalPrice variable that will keep track of the total price of all the sales
    double totalPrice = 0.00;
    // create a hashMap that will keep track of the items sold and their quantities, in totality
    HashMap<Item, Integer> totalItemsSold = new HashMap<Item, Integer>();

    // go through each sale
    for (Sale sale : salesLog.getAllSales()) {

      // print the name of the customer associated with the sale
      String name = sale.getUser().getName();
      System.out.println("Customer: " + name);
      // print the purchase number i.e the saleId
      System.out.println("Purchase Number: " + sale.getId());
      // print the purchase price
      System.out.println("Total Purchase Price: $" + sale.getTotalPrice());

      // add the total price to the sum of total price
      totalPrice += sale.getTotalPrice().doubleValue();
      // print the itemized breakdown
      System.out.println("Itemized Breakdown: ");
      // get the itemmized breakdown in a hashmap
      HashMap<Item, Integer> itemMap = sale.getItemMap();


      // go through each item and print its name and its quantity
      for (Entry<Item, Integer> entry : itemMap.entrySet()) {
        // get the item name, value, and print it
        Item key = entry.getKey();
        Integer value = entry.getValue();
        System.out.println("\t" + key.getName() + "  :  " + value);

        // add the quantity of the items sold to the total item list.

        // if the item already exists in the totalItemSold ,
        // then simply add to its quantity
        Item keyInOther = getItemInOther(totalItemsSold, key);
        if (keyInOther != null) {
          // add the previous quantity of the item to the total quantity.
          int prevq = totalItemsSold.get(keyInOther);
          totalItemsSold.put(keyInOther, prevq + value);
        } else {
          // add to the total items sold
          totalItemsSold.put(key, value);
        }
      }
      System.out.println("----------------------------------------------------------------");
    } // print the total items sold with their quantity
    for (Entry<Item, Integer> entry : totalItemsSold.entrySet()) {
      // get the Item name, value and print it
      Item key = entry.getKey();
      Integer value = entry.getValue();
      System.out.println("Number of  " + key.getName() + "  sold : " + value);
    }
    // print the total price
    BigDecimal finalPrice = BigDecimal.valueOf(totalPrice).add(new BigDecimal("0.00"));
    finalPrice.setScale(2);

    System.out.println("TOTAL SALES: $" + finalPrice);
    System.out.println("----------------------------------------------------------------");
  }

  /**
   * This method will return the Item "equivalent" in the hashmap given, based off its id.
   * 
   * @param hashMap - the hashmap we want to search for an item "equivalent".
   * @param item - the item we want to look for, whose id we will search for in the hashmap.
   * @return - the Item in the hashmap ("equivalent"), or null otherwise (invalid item).
   */

  public Item getItemInOther(HashMap<Item, Integer> hashMap, Item item) {
    // loop through hasmhap
    Item toReturn = null;
    for (Entry<Item, Integer> entry : hashMap.entrySet()) {

      // the key is the item
      Item key = entry.getKey();
      // if both ids match, we found the equivalent
      if (key.getId() == item.getId()) {
        return key;
      }
    }
    // otherwise here it was not found, which means item was not found in the hashmap.
    return toReturn;
  }

  /**
   * This method will display active/inactive accounts of a customer.
   * 
   * @param customerId - the id of the customer.
   * @param type - 1 for active, 2 for inactive.
   */
  public void displayAccounts(int customerId, int type) {
    // get the accounts from the DB
    List<Account> activeAccounts = DatabaseSelectHelper.getUserActiveAccounts(customerId, context);
    List<Account> inactiveAccounts = DatabaseSelectHelper.getUserInactiveAccounts(customerId, context);

    if (activeAccounts.isEmpty() && inactiveAccounts.isEmpty()) {
      System.out.println("This customer has no accounts.\n");
      return;
    }
    // if type is 1, then we display active
    if (type == 1) {
      // display active
      for (Account account : activeAccounts) {
        System.out.println("Account ID : " + account.getAccountId());
        System.out.println("Status : ACTIVE");
        System.out.println("-------------------------");
      }
    } else if (type == 2) {
      // display inactive
      for (Account account : inactiveAccounts) {

        System.out.println("Account ID : " + account.getAccountId());
        System.out.println("Status : INACTIVE");
        System.out.println("-------------------------");
      }
    }

  }


}
