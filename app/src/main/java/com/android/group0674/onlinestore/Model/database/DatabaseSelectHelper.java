package com.android.group0674.onlinestore.Model.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.group0674.onlinestore.Model.database.DatabaseDriverAndroid;
import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;
import com.android.group0674.onlinestore.Model.inventory.Inventory;
import com.android.group0674.onlinestore.Model.inventory.InventoryImpl;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.inventory.ItemImpl;
import com.android.group0674.onlinestore.Model.store.Sale;
import com.android.group0674.onlinestore.Model.store.SaleImpl;
import com.android.group0674.onlinestore.Model.store.SalesLog;
import com.android.group0674.onlinestore.Model.store.SalesLogImpl;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.Model.users.Account;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.Model.users.User;
import com.android.group0674.onlinestore.Model.users.UserFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the helper class for the DatabaseSelector.
 * 
 * @author dharmik
 *
 */
public class  DatabaseSelectHelper {

  /**
   * This method will return a list of the roles (ids) on the database.
   * 
   * @return - the list of roles, null if there was an error somewhere.
   */
  public static List<Integer> getRoleIds(Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<Integer> ids = new ArrayList<>();

    try {
      // try to get the roles from the DB
      results = database.getRoles();
      
      // loop through the results, and add the ids of each role to the arraylist
      while (results.moveToNext()) {
        // add the ids to the arraylist
        ids.add(results.getInt(results.getColumnIndex("ID")));
      }
    } catch (Exception e) {
      // close the ResultSet and Connection as there is an error
      try {
        results.close();
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }

    // close the ResultSet and Connection
    try {
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // if we reach here, all was good so return ids
    return ids;
  }

  /**
   * This method will return the name of the role, given a role id.
   * 
   * @param roleId - the id we want to get the name of.
   * @return - the name of the role, null otherwise (invalid role id).
   */
  public static String getRoleName(int roleId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    String role = null;
    try {
      // check if the role exists in the database
      role = database.getRole(roleId);
    } catch (Exception e) {
      // close the connection as there is an error
      try {
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }

    try {
      // close the connection
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return role;
  }

  /**
   * This method will return the roleId of a given user by their userId.
   * 
   * @param userId - the user who's roleId we want to return.
   * @return - the roleId of the user if successful, -1 in error (invalid userId).
   */
  public static int getUserRoleId(int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    int roleId;
    try {
      // check if we can find a role id with the given userId
      roleId = database.getUserRole(userId);
    } catch (Exception e) {
      // there is an error, close our connection
      try {
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      // try to close the connection
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the roleId
    return roleId;
  }

  /**
   * This method will return the roleId given the roleName.
   * 
   * @param roleName - the name of the role.
   * @return - the roleId associated with the role, -3 in error (invalid roleName).
   */
  public static Integer getRoleIdByName(String roleName, Context context) {
    // get a list of all the ids
    int roleId = -3;
    // get the list of role ids
    List<Integer> listOfRoleIds = DatabaseSelectHelper.getRoleIds(context);
    Log.d("the number of role ids are ", Integer.toString(listOfRoleIds.size()));
    // loop through this list, and check if the roleName matches
    for (Integer integer : listOfRoleIds) {
      if (DatabaseSelectHelper.getRoleName(integer, context).equals(roleName.toUpperCase())) {
        // if it does return this specific role id
        return integer;
      }
    }
    // if we reach here that means that the role wasn't found, so its -3 indicating an error
    return roleId;
  }

  /**
   * This method will return a list of user ids of users with a specific role id.
   * 
   * @param roleId - the list of users we want to have this specific role.
   * @return - the list of users with this roleId, null otherwise (invalid role id).
   */
  public static List<Integer> getUsersByRole(int roleId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<Integer> userIds = new ArrayList<>();
    try {
      // try to get the users by role from the DB
      results = database.getUsersByRole(roleId);
      
      // loop over the results and add it to our array list
      while (results.moveToNext()) {
        // add it to the arraylist
        userIds.add(results.getInt(results.getColumnIndex("USERID")));
      }
    } catch (Exception e) {
      // close the ResultSet and Connection as there is an error
      try {
        results.close();
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }
    // close the ResultSet and Connection
    try {
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the userIds
    return userIds;
  }

  /**
   * This method will return a list of Users objects (specific if they have a role).
   * 
   * @return - the list of User objects, null if any error occurs.
   */
  public static List<User> getUsersDetails(Context context) {

    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<User> users = new ArrayList<>();

    try {
      // try to get the users
      results = database.getUsersDetails();
      
      while (results.moveToNext()) {
        // figure out the type of user and create a User object based on the info
        User userObj = specificUserAndDetails(results.getInt(results.getColumnIndex("ID")),
                results.getString(results.getColumnIndex("NAME")),
                results.getInt(results.getColumnIndex("AGE")),
                results.getString(results.getColumnIndex("ADDRESS")), context);
        // add to the arraylist
        users.add(userObj);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // try to close the ResultSet and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the users
    return users;
  }

  /**
   * This method will return a User object (or any of its sub types) based on its information.
   * 
   * @param userId - the id of the user.
   * @param name - the name of the user.
   * @param age - the age of the user.
   * @param address - the address of the user.
   * @return - an Employee, Customer, or null, depending on the user role.
   */
  private static User specificUserAndDetails(int userId, String name, int age, String address, Context context) {
    User user = UserFactory.getUser(userId, name, age, address, context);
    return user;
  }

  /**
   * This method will return a User object of a specific user, based off their userId.
   * 
   * @param userId - the id of the user we want the details of.
   * @return - the user object (Employee, Customer), or null otherwise (invalid user id).
   */
  public static User getUserDetails(int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    User userObj = null;
    try {

      // try to get the results
      results = database.getUserDetails(userId);
      //
      // set the count to 0
      int count = 0;

      while (results.moveToNext()) {

        // increase the count
        count += 1;
        // figure out the type of user and create a User object based on the info
        userObj = specificUserAndDetails(results.getInt(results.getColumnIndex("ID")),
                results.getString(results.getColumnIndex("NAME")),
                results.getInt(results.getColumnIndex("AGE")),
                results.getString(results.getColumnIndex("ADDRESS")), context);

        Log.d("the id was", Integer.toString(results.getInt(results.getColumnIndex("ID"))));
        Log.d("the name was", results.getString(results.getColumnIndex("NAME")));
        Log.d("the age was", Integer.toString(results.getInt(results.getColumnIndex("AGE"))));
        Log.d("the address was", results.getString(results.getColumnIndex("ADDRESS")));


      }
      // if the count is 0, that means no results were found, and so the id is invalid
      if (count == 0) {
        try {
          // try to close the connection
          results.close();
          database.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        // return null to indicate there was an error in the inputs
        return null;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // try to close the connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the user object
    return userObj;
  }

  /**
   * This method will return true/false depending on whether a user exists with given userId.
   * 
   * @param userId - the id we want to test to see exists in the database.
   * @return - true if it does, false otherwise
   */
  public static boolean checkIfUserExists(int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;

    // try to get the results
    try {
      results = database.getUserDetails(userId);
      
      if (!results.moveToFirst()) {
        // a user does not exist with this id
        return false;
      }
    } catch (Exception e) {
      // return false to indicate there was an error in the inputs
      return false;
    }
    // close the connection
    try {
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // if we reach here that means it does exist
    return true;
  }

  /**
   * This method will return the hashed password of a user based off their userId.
   * 
   * @param userId - the id of the User we want the password of.
   * @return - the hashed password, null otherwise (invalid userId).
   */
  public static String getPassword(int userId, Context context) {
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    String password;
    try {
      // try to get the password
      password = database.getPassword(userId);
    } catch (Exception e) {
      // there was an error, close our connection
      try {
        // try to close the connection
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }
    try {
      // try to close the connection
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the password
    return password;
  }

  /**
   * This method will return a list of all the items in the database.
   * 
   * @return - a list of all the items in the database, null if any error occurs.
   */
  public static List<Item> getAllItems(Context context) {
    // create a connection to the server
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<Item> items = new ArrayList<>();
    try {
      // try to get the results
      results = database.getAllItems();
      
      // loop over the results
      while (results.moveToNext()) {
        // add item to list
        Item item = new ItemImpl(results.getInt(results.getColumnIndex("ID")),
                results.getString(results.getColumnIndex("NAME")),
                new BigDecimal(results.getString(results.getColumnIndex("PRICE"))));
        // add to our list
        items.add(item);
      }
    } catch (Exception e) {
      // return null to indicate there was an error in the inputs
      return null;
    }

    try {
      // close our results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the list of items
    return items;
  }

  /**
   * This method will return the item based off the itemId.
   * 
   * @param itemId - the id of the item we want to return.
   * @return - the item, or null otherwise (itemId does not exist).
   */
  public static Item getItem(int itemId, Context context) {
    // create a new connection
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results;
    Item item = null;
    try {
      // try to get the results
      results = database.getItem(itemId);
      
      // create the item
      while (results.moveToNext()) {
        item = new ItemImpl(itemId, results.getString(results.getColumnIndex("NAME")),
                new BigDecimal(results.getString(results.getColumnIndex("PRICE"))));
      }

    } catch (Exception e) {
      // return null to indicate there was an error in the inputs
      return null;
    }

    try {
      // close our results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the item
    return item;
  }

  /**
   * This method will return the inventory stored on the database.
   * 
   * @return - the inventory, null otherwise (error in accessing).
   */
  public static Inventory getInventory(Context context) {
    // create connection with database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    // create an inventory and hashmap to store its items
    Inventory inventory = new InventoryImpl();
    HashMap<Item, Integer> items = new HashMap<>();
    try {
      // try to get results
      results = database.getInventory();
      
      // loop through the results
      while (results.moveToNext()) {
        // add to hashmap
        items.put(DatabaseSelectHelper.getItem(results.getInt(results.getColumnIndex("ITEMID")),
                context),
            Integer.parseInt(results.getString(results.getColumnIndex("QUANTITY"))));
        // also set the total number of items in the inventory
        inventory.setTotalItems(Integer.parseInt(results.getString(results.getColumnIndex("QUANTITY"))));
      }
      // set it
      inventory.setItemMap(items);

    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      // try to close the results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the inventory
    return inventory;
  }

  /**
   * This method will return the number of items in the inventory given an item id.
   * 
   * @param itemId - the item we want to check the quantity of in the database.
   * @return - the quantity of this item, -1 otherwise (invalid itemId).
   */
  public static int getInventoryQuantity(int itemId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    int quantity;
    try {
      // try to get the quantity
      quantity = database.getInventoryQuantity(itemId);
    } catch (Exception e) {
      // return -1 to indicate there was an error in the inputs
      return -1;
    }
    try {
      // try to close the connection
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the quantity
    return quantity;
  }

  /**
   * This method will return a SalesLog of all the sales that have taken place.
   * 
   * @return - a SalesLog of all the sales, null if any error occurs.
   */
  public static SalesLog getSales(Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    // create a salesLog object
    Cursor results = null;
    SalesLog salesLog = new SalesLogImpl();

    try {
      // try to get the results
      results = database.getSales();
      
      // loop through, and add all the sales
      while (results.moveToNext()) {
        // add to our salesLog
        salesLog.addSale(new SaleImpl(results.getInt(results.getColumnIndex("ID")),
                results.getInt(results.getColumnIndex("USERID")),
                new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))), context));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // try to close results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the saleslog
    return salesLog;
  }

  /**
   * This method will return a Sale object given by a saleId.
   * 
   * @param saleId - the id of the Sale we want to return.
   * @return - Sale object if successful, null otherwise (invalid saleId).
   */
  public static Sale getSaleById(int saleId, Context context) {
    // create connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results;
    Sale sale = null;
    try {
      // try to get the result
      results = database.getSaleById(saleId);
      
      int count = 0;
      while (results.moveToNext()) {
        // increase our count as there is a sale
        count += 1;
        // create the sale object
        sale = new SaleImpl(results.getInt(results.getColumnIndex("ID")),
                results.getInt(results.getColumnIndex("USERID")),
                new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))), context);
        // set this sales item map
        DatabaseSelectHelper.getItemizedSaleById(saleId, sale, context);
      }
      // if our count is still 0, that means that the saleId does not exist
      if (count == 0) {
        // return null to indicate there was an error in the inputs
        return null;
      }

    } catch (Exception e) {
      // return null to indicate there was an error in the inputs
      return null;
    }
    try {
      // try to close results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the sale
    return sale;
  }

  /**
   * This method will return a list of Sales specific to a certain user based off their userId.
   * 
   * @param userId - the id of the user we want the list of sales of.
   * @return - the list of Sales if successful, null otherwise (invalid userId).
   */
  public static List<Sale> getSalesToUser(int userId, Context context) {
    // create connection to database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    List<Sale> sales = new ArrayList<>();
    Cursor results;
    try {
      // try to get result
      results = database.getSalesToUser(userId);
      
      // loop through all the sales
      while (results.moveToNext()) {
        // add the sale to list
        sales.add(new SaleImpl(results.getInt(results.getColumnIndex("ID")),
                results.getInt(results.getColumnIndex("USERID")),
                new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))), context));
      }
    } catch (Exception e) {
      // return null to indicate there was an error in the inputs
      return null;
    }

    try {
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the sales
    return sales;
  }

  /**
   * This method sets the item map of a specific sale, given its sale id.
   * 
   * @param saleId - the id of the sale.
   * @param sale - the sale object itself, whose item map we want to change.
   * @throws InvalidArgumentException - if we have an invalid saleId.
   * @throws NullPointerException - if the sale object is null.
   */
  public static void getItemizedSaleById(int saleId, Sale sale, Context context)
      throws InvalidArgumentException, NullPointerException {
    // create connection with database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    // create a hashmap to be map the items to the amount sold
    HashMap<Item, Integer> itemMap = new HashMap<>();
    try {
      // try to get results
      results = database.getItemizedSaleById(saleId);
      while (results.moveToNext()) {
        // add the information to the hashmap
        itemMap.put(DatabaseSelectHelper.getItem(results.getInt(results.getColumnIndex("ITEMID")), context),
                results.getInt(results.getColumnIndex("QUANTITY")));
      }
      // add the mapping to the sale
      sale.setItemMap(itemMap);

    } catch (NullPointerException e) {
      throw new NullPointerException("Error: the sale was null");
    } catch (Exception e) {
      throw new InvalidArgumentException("Error: saleId does not exist");
    }

    try {
      // try to close results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // no return, we just want to update the mapping in the sales object
  }

  /**
   * This method will map all the sales from SalesLog, their items to the quantity sold.
   * 
   * @param salesLog - the log containing all the sales.
   * @throws NullPointerException - if the salesLog is null.
   */
  public static void getItemizedSales(SalesLog salesLog, Context context)
          throws NullPointerException {
    // create connection to database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    try {
      // try to get results
      results = database.getItemizedSales();
      
      // loop through all the sales
      while (results.moveToNext()) {
        // call the getItemizedSaleById function on each specific sale
        // get the sale id
        int saleId = results.getInt(results.getColumnIndex("SALEID"));
        DatabaseSelectHelper.getItemizedSaleById(results.getInt(results.getColumnIndex("SALEID")),
            salesLog.getAllSales().get(saleId - 1), context);

      }

    } catch (NullPointerException e) {
      throw new NullPointerException("salesLog is null");
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    }

    try {
      // try to close results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * This method will return the itemId of a given item by their name.
   * 
   * @param name - the name of the item.
   * @return - the itemId of the item if successful, -3 in error (not found).
   */
  public static int getItemIdByName(String name, Context context) {
    // get a list of all the ids
    int itemId = -3;
    // get the list of item ids
    List<Item> listOfItems = DatabaseSelectHelper.getAllItems(context);
    Log.d("THE ITEM NAME IS------------------------------------- ", name);
    // loop through this list, and check if the itemName matches
    for (Item item : listOfItems) {
      if (item.getName().equals(name)) {
        // return 1 to indiciate it was found
        return item.getId();
      }
    }
    // if we reach here that means that the role wasn't found, so its -3 indicating an error
    return itemId;
  }

  /**
   * This method will check to see if the account exists in the database.
   * 
   * @param customerId - the id of the customer we want to check.
   * @return true or false - depending on whether the account exists or not.
   */
  public static int checkIfAccountExists(int customerId, Context context) {
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    int id = -1;


    Cursor results = null;
    // try to get results
    try {

      Log.d("DATA IS NULL AND RESULTSI S NULL" , Boolean.toString(database == null) + "    " +
              Boolean.toString(results == null));
      results = database.getUserAccounts(customerId);
      Log.d("out of the function , and it is null: ", Boolean.toString(results == null));
      
      while (results.moveToNext()) {
        Log.d("I GO IN THE LOOP IN CHECKIFACCOUNTEXISTS","asd");
        // if this value is updated, then we know an account exists
        id = results.getInt(results.getColumnIndex("ID"));
        Log.d("THE ACCOUNT ID IS " , Integer.toString(id));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      // try to close results and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // if we reach here, and id = -1, then it doesn't exist
    Log.d("RETURNING ID" , Integer.toString(id));


    return id;
  }

  /**
   * This method will restore the ShoppingCart of a customer from their account.
   * 
   * @param accountId - the id of their account.
   * @param customer - the actual customer object.
   * @return - the ShoppingCart (old one), or new if they don't have account.
   * @throws InvalidArgumentException - if the customer is invalid.
   */
  public static ShoppingCart restoreShoppingCart(int accountId, Customer customer, Context context)
      throws InvalidArgumentException {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    ShoppingCart shoppingCart = new ShoppingCart(customer, context);
    // create our counter variable to see if they have a previous shopping cart

    int count = 0;
    try {
      // try to get the users
      results = database.getAccountDetails(accountId);

      Log.d("we are trying to find out info from account id, and reuslts is null " , Integer.toString(accountId) + "  " +
      Boolean.toString(results == null));
      Log.d("the account existso n the database: ", Integer.toString(DatabaseSelectHelper.checkIfAccountExists(customer.getId(), context)));
      //Log.d("the quantity is " , Integer.toString(results.getInt(results.getColumnIndex("quantity"))));
      while (results.moveToNext()) {
        // add it to the hashmap

        Log.d("IN THE LOOP FOR RESULTS MOVE TO NEXT","");
        shoppingCart.addItem(DatabaseSelectHelper.getItem(results.getInt(results.getColumnIndex("ITEMID")), context),
            results.getInt(results.getColumnIndex("QUANTITY")));
        count++;
      }
    } catch (Exception e) {

      e.printStackTrace();
    }

    try {
      // try to close the ResultSet and connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // if the count is still 0, that means they don't have any shopping history
    if (count == 0) {
      System.out.println("There is no shopping history, please check out first.\n");
    } else {
      System.out.println("Shopping cart restored.");
    }
    return shoppingCart;
  }

  /**
   * This method will return an Account object, based off the given accountId.
   * 
   * @param userId
   * 
   * @param accountId the account id whose details we want.
   * @return the account associated with the account Id
   */
  public static Account getAccountDetails(int accountId, int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    Account account = null;
    try {

      // try to get the results
      results = database.getAccountDetails(accountId);
      
      // set the count to 0

      HashMap<Item, Integer> itemMap = new HashMap<>();

      int count = 0;

      while (results.moveToNext()) {
        // increase the count
        count += 1;
        itemMap.put(getItem(results.getInt(results.getColumnIndex("ITEMID")), context),
                results.getInt(results.getColumnIndex("QUANTITY")));

      }
      // create the account and set the itemMap
      account = new Account(accountId, userId, context);
      account.setItemMap(itemMap);
      // no wait, this method is the wrong one, this method just gets the account details for a
      // specific
      // acount id, we have no way of checking whether its active or not
      // thats why we do this: wait, wait, let me just check it with an actual example.

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // try to close the connection
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the user object
    return account;
  }

  /**
   * This Method will return a list a accounts related to the given userId
   * 
   * @param userId the userId whos accounts are to be returned.
   * @return list of accounts associated with that userId.
   */
  public static List<Account> getUserAccounts(int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<Account> accounts = new ArrayList<>();
    try {
      // try to get the users by role from the DB
      results = database.getUserAccounts(userId);
      
      // loop over the results and add it to our array list
      while (results.moveToNext()) {
        // add it to the arraylist
        accounts.add(getAccountDetails(results.getInt(results.getColumnIndex("ID")), userId,
                context));
      }
    } catch (Exception e) {
      // close the ResultSet and Connection as there is an error
      try {
        results.close();
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }
    // close the ResultSet and Connection
    try {
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the userIds
    return accounts;
  }

  /**
   * This method will return a list of accounts that are associated with a given user that are
   * active.
   * 
   * @param userId whos inactive accounts we want.
   * @return the list of inactive accounts that the specific user has.
   */
  public static List<Account> getUserInactiveAccounts(int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<Account> accounts = new ArrayList<>();
    try {
      // try to get the users by role from the DB
      results = database.getUserInactiveAccounts(userId);
      
      // loop over the results and add it to our array list
      while (results.moveToNext()) {
        // add it to the arraylist
        Account account = getAccountDetails(results.getInt(results.getColumnIndex("ID")), userId,
                context);
        account.setAccountStatus(false);
        accounts.add(account);
      }
    } catch (Exception e) {
      // close the ResultSet and Connection as there is an error
      try {
        results.close();
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }
    // close the ResultSet and Connection
    try {
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the userIds
    return accounts;
  }

  /**
   * This method will return a list of accounts that are associated with a given user that are
   * active.
   * 
   * @param userId whose active accounts we want.
   * @return the list of active accounts that the specific user has.
   */
  public static List<Account> getUserActiveAccounts(int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid database = new DatabaseDriverAndroid(context);
    Cursor results = null;
    List<Account> accounts = new ArrayList<>();
    try {
      // try to get the users by role from the DB
      results = database.getUserActiveAccounts(userId);
      
      // loop over the results and add it to our array list
      while (results.moveToNext()) {
        // add it to the arraylist
        Account account = getAccountDetails(results.getInt(results.getColumnIndex("ID")), userId,
                context);
        account.setAccountStatus(true);
        accounts.add(account);
      }
    } catch (Exception e) {
      // close the ResultSet and Connection as there is an error
      try {
        results.close();
        database.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      // return null to indicate there was an error in the inputs
      return null;
    }
    // close the ResultSet and Connection
    try {
      results.close();
      database.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the userIds
    return accounts;
  }

}
