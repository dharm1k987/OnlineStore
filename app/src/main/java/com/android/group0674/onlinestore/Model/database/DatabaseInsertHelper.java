package com.android.group0674.onlinestore.Model.database;

import com.android.group0674.onlinestore.Model.exceptions.DatabaseInsertException;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseMissingTableException;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.inventory.ItemTypes;
import com.android.group0674.onlinestore.Model.store.Sale;
import com.android.group0674.onlinestore.Model.users.Roles;
import com.android.group0674.onlinestore.Model.users.User;
import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This is the helper class for the DatabaseInserter.
 * 
 * @author dharmik
 *
 */
public class DatabaseInsertHelper {

  /**
   * This method inserts a role into the database.
   * 
   * @param name - the name of the role (database will be all capitals).
   * @param context - the appcontext needed to access the database
   * @return - an int indicating the role id, -1 in error (role name not in enum).
   */
  public static int insertRole(String name, Context context) {

    // create connection with database
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);

    // check whether the role comes from the enum Roles and its not already in database
    boolean validRole = checkIfValidRole(name) && DatabaseSelectHelper.getRoleIdByName(name, context) == -3;
    long roleId = 0;
    try {
      if (validRole) {
        // insert the role
        roleId = db.insertRole(name.toUpperCase());
      } else {
        // return -1 to indicate there was an error in the inputs
        return -1;
      }
    } catch (Exception e) {
      return -1;
    }
    // close the connection
    try {
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) roleId;
  }

  /**
   * This method checks to see if the given name for a role is valid (must come from enum).
   * 
   * @param name - the name of the role wished to be inserted.
   * @return true or false depending on whether its valid or not.
   */
  protected static boolean checkIfValidRole(String name) {
    // if name is null, then it is automatically false
    if (name == null) {
      return false;
    }
    // loop through all the roles in the enum, and see if we have a match
    for (Roles role : Roles.values()) {
      if (role.name().equals(name.toUpperCase())) {
        // if match return true
        return true;
      }
    }
    // if we reach here there was no match, so return false
    return false;
  }

  /**
   * This method inserts a new user into the database, with the specified parameters.
   * 
   * @param name - a non-empty string consisting of at least first and last name.
   * @param age - the age of the user, must be >= 0.
   * @param context - the appcontext needed to access the database
   * @param address - a non-empty string which is the address.
   * @param password - a non-empty string.
   * @return - the new userId if all was successful, -1 in error (wrong format or null inputs).
   */
  public static int insertNewUser(String name, int age, String address, String password, Context context) {

    // create connection with database
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);

    // if any of our inputs were null, return -2 to indicate there was a null
    if ((name == null) || (address == null) || (password == null)) {
      // return -2 to indicate there was an error in the inputs (null)
      return -2;
      // other cases we consider as bad input
    } else if ((age < 0) || address.length() > 100 || address.isEmpty()
        || name.trim().indexOf(" ") == -1) {
      try {
        // close the connection since we have an error
        db.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      // return -1 to indicate there was an error in the inputs
      return -1;
    }
    // otherwise we are fine, and we will insert
    long userId = db.insertNewUser(name, age, address, password);

    try {
      // close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the userId
    return (int) userId;
  }

  /**
   * This method inserts a role to a specific user based off its id and the role id.
   * 
   * @param userId - the id of the user you wish to insert a role to.
   * @param roleId - the id of the role you wish the user to have.
   * @param context - the appcontext needed to access the database
   * @return - the userRoleId if all was successful, -1 in error (userId or roleId does not exist).
   */
  public static int insertUserRole(int userId, int roleId, Context context) {

    // create connection with database
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);

    long userRoleId = 0;
    boolean valid;

    // check if the inputs are valid
    valid = checkIfValidUserIdAndRoleId(userId, roleId, context);
    // if they are valid
    if (valid) {
      // then we want to insert the role if it is valid
      try {
        // try to insert it into the database
        userRoleId = db.insertUserRole(userId, roleId);
        User user = DatabaseSelectHelper.getUserDetails(userId, context);
        user.setRoleId(roleId);
      } catch (Exception e) {
        // return -1 to indicate there was an error in the inputs
        return -1;
      }
      // if not valid, error
    } else {
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      // close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) userRoleId;
  }

  /**
   * This method checks to see if the userId and roleId exist in the database.
   * 
   * @param userId - the id of the user.
   * @param roleId - the id of the role.
   * @param context - the appcontext needed to access the database
   * @return - true if they both exist, false otherwise.
   */
  private static boolean checkIfValidUserIdAndRoleId(int userId, int roleId, Context context) {
    // get a list of the users and the role ids
    boolean userIdValid;
    // check if the userId exists
    userIdValid = DatabaseSelectHelper.checkIfUserExists(userId, context);
    boolean roleIdValid = false;

    // get a list of the roleIds
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds(context);
    if (roleIds == null) {
      return false;
    }

    // check if any of the role ids matched
    for (Integer id : roleIds) {
      if (id == roleId) {
        // if there was a match its true
        roleIdValid = true;
      }
    }
    // return whether both conditions are true
    return (roleIdValid && userIdValid);
  }

  /**
   * This method inserts an Item and specified price for it into the database inventory.
   * 
   * @param name - the name of the item.
   * @param price - the price of the item (> 0)
   * @param context - the appcontext needed to access the database
   * @return - the itemId if was successful, -1 in error (name is not in item enum, price is not
   *         properly formatted).
   */
  public static int insertItem(String name, BigDecimal price, Context context) throws DatabaseInsertException {
    // create a connection
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    long itemId = 0;
    // check whether the role comes from the enum Roles and its not already in the database
    boolean validItem =
        checkIfValidItemAndPrice(name, price) && DatabaseSelectHelper.getItemIdByName(name, context) == -3;
    if (validItem) {
      // insert the role
      itemId = db.insertItem(name, price);
    } else {
      // close the connection
      try {
        // try to close the connection
        db.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) itemId;
  }

  /**
   * This method checks to see if an item is in the item enum and if the price is properly
   * formatted.
   * 
   * @param name - the name of the item.
   * @param price - the price of the item (only two decimals, number > 0).
   * @return - true if valid, false otherwise.
   */
  protected static boolean checkIfValidItemAndPrice(String name, BigDecimal price) {
    // if name is null, then it is automatically false
    if (name == null) {
      return false;
      // if the big decimal is not formatted properly or is <= 0
    } else if (price.compareTo(BigDecimal.ZERO) <= 0) {
      return false;
    } else if (numberOfDecimalPlaces(price) != 2) {
      return false;
    }

    // loop through all the items in the enum, and see if we have a match
    for (ItemTypes item : ItemTypes.values()) {
      if (item.name().equals(name.toUpperCase())) {
        // if we find a match return true
        return true;
      }
    }
    // if we reach here then we are false
    return false;
  }

  /**
   * This method calculates the number of decimal places for a given price.
   * 
   * @param price - the big decimal price to be checked.
   * @return - the number of decimal places. -1 if none (invalid input).
   */
  private static int numberOfDecimalPlaces(BigDecimal price) {
    // convert to string and find index of the dot (.)
    String stringRepr = price.toPlainString();
    int findDecimalIndex = stringRepr.indexOf(".");
    // if the dot was not found, that means it was not found.
    if (findDecimalIndex < 0) {
      return -1;
    }
    // otherwise return the amount of places.
    return stringRepr.length() - findDecimalIndex - 1;
  }

  /**
   * This method inserts a item and quantity into the database inventory.
   * 
   * @param itemId - the itemId of the Item you wish to insert.
   * @param quantity - the amount of items to insert.
   * @param context - the appcontext needed to access the database
   * @return - the inventory id if was successful, -1 in error.
   */
  public static int insertInventory(int itemId, int quantity, Context context) {

    // create a new connection
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);
    long inventoryId = 0;
    // check whether the itemId and quantity is valid
    boolean valid = checkIfValidItemIdAndQuantity(itemId, quantity, context);
    // if valid
    if (valid) {
      // insert into the inventory
      inventoryId = db.insertInventory(itemId, quantity);
    } else {
      try {
        // try to close the connection
        db.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) inventoryId;
  }

  /**
   * This method checks to see if an itemId corresponds to an item and a quantity.
   * 
   * @param itemId - the id of the Item.
   * @param quantity - a number > 0.
   * @param context - the appcontext needed to access the database
   * @return - true if we can find an Item with this id and proper quantity, false otherwise.
   */
  private static boolean checkIfValidItemIdAndQuantity(int itemId, int quantity, Context context) {
    // get the item that the itemId corresponds to
    if (DatabaseSelectHelper.getItem(itemId, context) == null) {
      // return false to indicate there was an error in the inputs (id not valid)
      return false;
    }
    // if we reach here that means that id was correct, now just check quantity
    if (quantity <= 0) {
      return false;
    }
    return true;
  }

  /**
   * This methods inserts a sale into the database of a user.
   * 
   * @param userId - the id of the user who purchased.
   * @param totalPrice - the total price of the sale.
   * @param context - the appcontext needed to access the database
   * @return - the saleId if successful, false otherwise (invalid userId or totalPrice).
   */
  public static int insertSale(int userId, BigDecimal totalPrice, Context context){
    // create a connection to the database
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);
    boolean valid;
    long saleId;
    Log.d("in insert sale, the user id is " , Integer.toString(userId)+ "   " + totalPrice.toString());
    // try to see if the input is valid
    valid = checkIfValidUserIdAndPrice(userId, totalPrice, context);

    Log.d("the sale has been VALID" , Boolean.toString(valid));

    // if valid, then insert the sale
    if (valid) {
      saleId = db.insertSale(userId, totalPrice);
    } else {
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) saleId;
  }

  /**
   * This method checks to see if a userId refers to a User and if the totalPrice is a valid amount.
   * 
   * @param userId - the id corresponding to a user.
   * @param totalPrice - the price of the sale.
   * @param context - the appcontext needed to access the database
   * @return - true if both are valid (userId and valid price), else false (no user, invalid price).
   */
  private static boolean checkIfValidUserIdAndPrice(int userId, BigDecimal totalPrice, Context context) {
    // check if the userId is valid
    boolean userIdValid;
    userIdValid = DatabaseSelectHelper.checkIfUserExists(userId, context);
    Log.d("in the method, the USER ID is valid: ", Boolean.toString(userIdValid));
    // check if totalPrice is formatted properly
    if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
      return false;
    } else if (numberOfDecimalPlaces(totalPrice) != 2) {
      return false;
    }
    // if it is, then just return whether the userId is valid since price is valid from above
    return userIdValid;
  }

  /**
   * This method inserts a itemized sale into the database.
   * 
   * @param saleId - the id of the sale that occurred.
   * @param itemId - the id of the item that was sold.
   * @param quantity - the amount of items that was sold.
   * @param context - the appcontext needed to access the database
   * @return - the itemizedId if successful, false otherwise (invalid saleId, itemId or quantity).
   */
  public static int insertItemizedSale(int saleId, int itemId, int quantity, Context context) {
    // try to get the connection
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);
    boolean valid;
    long itemizedId = 0;

    try {
      // check if the combination is correct
      valid = checkIfCombinationCorrect(saleId, itemId, quantity, context);
      // if it is valid
      if (valid) {
        // get the itemized id
        itemizedId = db.insertItemizedSale(saleId, itemId, quantity);
      } else {
        // return -1 to indicate there was an error in the inputs
        return -1;
      }
    } catch (Exception e) {
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      // close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return the itemized id
    return (int) itemizedId;
  }

  /**
   * This method checks to see if the combination of saleId, itemId and quantity is valid.
   * 
   * @param saleId - the id of the sale that occurred.
   * @param itemId - the id of the item that was sold.
   * @param quantity - the amount of that item that was sold.
   * @param context - the appcontext needed to access the database
   * @return - true if the sales total price = item price * quantity, false otherwise
   */
  private static boolean checkIfCombinationCorrect(int saleId, int itemId, int quantity, Context context) {
    // get the Sale associated with the sale id
    // could also use saleslog
    Sale sale = DatabaseSelectHelper.getSaleById(saleId, context);
    // get the Item associated with the item id
    Item item = DatabaseSelectHelper.getItem(itemId, context);
    // check if the total is what is expected
    BigDecimal totalPrice = sale.getTotalPrice();
    // calculuate how much is paying right now
    BigDecimal pricePaidNow = item.getPrice().multiply(new BigDecimal(quantity));

    // if total price is greater then or equal to what we have, we are OK
    return totalPrice.compareTo(pricePaidNow) == 0 || totalPrice.compareTo(pricePaidNow) == 1;

  }

  /**
   * This methods inserts a account into the database of a user (customer).
   * 
   * @param userId - the id of the customer who we want to insert the account.
   * @param context - the appcontext needed to access the database
   * @return - the accountId if successful, false otherwise (invalid userId).
   */
  public static int insertAccount(int userId, boolean active, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);
    boolean valid;
    long accountId;
    User user = DatabaseSelectHelper.getUserDetails(userId, context);
    // try to see if the userId is valid & if they are a customer
    valid = user != null && user.getRoleId() == DatabaseSelectHelper.getRoleIdByName("CUSTOMER",
            context);

    // if valid, then insert the sale
    if (valid) {
      accountId = db.insertAccount(userId, active);
    } else {
      // return -1 to indicate there was an error in the inputs
      return -1;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) accountId;
  }

  /**
   * This methods inserts an item into an account.
   * 
   * @param accountId - the id of the customer who we want to insert the account.
   * @param itemId - the id of the item.
   * @param quantity - the number of this item bought.
   * @param context - the appcontext needed to access the database
   * @return - the id of the inserted record if successful, false otherwise (invalid userId).
   */
  public static int insertAccountLine(int accountId, int itemId, int quantity, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid db  = new DatabaseDriverAndroid (context);
    // our error code
    long recordId = -2;
    try {
      // try inserting
      Log.d("I WILL TRY TO INSERT THE INFO WITH ACCOUNT ID, ITEMID, QUANTITY, ",
      Integer.toString(accountId)+ "   " + Integer.toString(itemId) + "  " + Integer.toString(quantity));
      recordId = db.insertAccountLine(accountId, itemId, quantity);
      Log.d("the record id is ", Long.toString(recordId));
      Log.d("Hey the item is", Integer.toString(DatabaseSelectHelper.getItem(itemId,context).getId()));
    } catch (Exception e1) {
        e1.printStackTrace();
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) recordId;
  }
}
