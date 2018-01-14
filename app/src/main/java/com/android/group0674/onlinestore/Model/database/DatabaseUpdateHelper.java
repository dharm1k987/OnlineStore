package com.android.group0674.onlinestore.Model.database;

import java.math.BigDecimal;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the helper class for DatabaseUpdater.
 * 
 * @author dharmik
 *
 */
public class DatabaseUpdateHelper {

  /**
   * This method updates the name of a role in the database.
   * 
   * @param name - the name of the role (new one).
   * @param id - the id of the existing role we wish to replace.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid name or id)
   */
  public static boolean updateRoleName(String name, int id, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);

    boolean complete;
    boolean valid;
    if (name == null || name.isEmpty()) {
      // return false to indicate there was an error in the inputs
      return false;
    }
    try {
      // check to see if the role exists in enum and if the id corresponds to a real role
      valid = DatabaseInsertHelper.checkIfValidRole(name)
          && DatabaseSelectHelper.getRoleName(id, context) != null;
      if (valid) {
        // if valid, update it
        complete = db.updateRoleName(name, id);
      } else {
        // return false to indicate there was an error in the inputs
        return false;
      }
    } catch (NullPointerException e) {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return complete;
  }

  /**
   * This method updates the name of a user, based off its id.
   * 
   * @param name - the new name of the user.
   * @param userId - the id of the user.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid name or id).
   */
  public static boolean updateUserName(String name, int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean complete = false;
    if (name == null) {
      // return false to indicate there was an error in the inputs
      return false;
    }
    boolean valid;
    // check if user exists and if name is valid
    valid = DatabaseSelectHelper.checkIfUserExists(userId, context) && name.trim().indexOf(" ") != -1;
    // if valid, insert it
    if (valid) {
      complete = db.updateUserName(name, userId);
    } else {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;
  }

  /**
   * This method updates the age of a user, based off its id.
   * 
   * @param age - the new age of the user (> 0).
   * @param userId - the id of the user we want to change.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid age or id).
   */
  public static boolean updateUserAge(int age, int userId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean valid;
    boolean complete = false;
    // check if valid age and if user exists
    valid = age > 0 && DatabaseSelectHelper.checkIfUserExists(userId, context);
    if (valid) {
      // update the information
      complete = db.updateUserAge(age, userId);
    } else {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;
  }

  /**
   * This method updates a user address, based off their userId.
   * 
   * @param address - the new address of the user.
   * @param userId - the id of the user we want to update.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid address or userId).
   */
  public static boolean updateUserAddress(String address, int userId, Context context) {
    // create a connection with the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean complete;
    // address is not valid if its too long, empty, or null
    boolean notValid = address.length() > 100 || address.isEmpty() || address == null;
    if (!notValid) {
      // if valid, then update the user
      complete = db.updateUserAddress(address, userId);
    } else {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;

  }

  /**
   * This method updates the role of a user in the database, based off its userId.
   * 
   * @param roleId - the id of the role we want to set the user to.
   * @param userId - the id of the user we want to update.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid roleId or userId).
   */
  public static boolean updateUserRole(int roleId, int userId, Context context) {
    // create a connection with the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean valid;
    boolean complete = false;
    try {
      // try to see if valid user and if roleId is one of the allowed ones
      valid = DatabaseSelectHelper.checkIfUserExists(userId, context)
          && DatabaseSelectHelper.getRoleIds(context).contains(roleId);
      // if valid, update the user
      if (valid) {
        complete = db.updateUserRole(roleId, userId);
      } else {
        // otherwise its not
        return false;
      }
    } catch (NullPointerException e) {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      // try to close connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;
  }

  /**
   * This method updates the name of an item in the database.
   * 
   * @param name - the new name of the item.
   * @param itemId - the id of the item we want to change the name of.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid name or itemId).
   */
  public static boolean updateItemName(String name, int itemId, Context context) {
    // create a connection with the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean complete;
    // valid when name is non-empty, non-null, and is one of the items
    boolean valid =
        name != null && (!name.isEmpty()) && DatabaseSelectHelper.getItem(itemId, context) != null
            && DatabaseInsertHelper.checkIfValidItemAndPrice(name, new BigDecimal("0.01"));
    // if valid, update the item name
    if (valid) {
      complete = db.updateItemName(name, itemId);
    } else {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;

  }

  /**
   * This method updates the item price based off its itemId.
   * 
   * @param price - the new price of the item.
   * @param itemId - the id of the Item we want to change the price of.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid price or itemId).
   */
  public static boolean updateItemPrice(BigDecimal price, int itemId, Context context) {
    // create a connection to the database
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean valid;
    boolean complete;
    // valid when
    valid = DatabaseInsertHelper
        .checkIfValidItemAndPrice(DatabaseSelectHelper.getItem(itemId, context).getName(), price);
    if (valid) {
      // update the item price
      complete = db.updateItemPrice(price, itemId);
    } else {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;
  }

  /**
   * This method updates the inventory quantity of a specific item.
   * 
   * @param quantity - the new quantity to be set.
   * @param itemId - the id of the item we want to change the quantity of.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid quantity or itemId).
   */
  public static boolean updateInventoryQuantity(int quantity, int itemId, Context context) {
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean valid;
    boolean complete = false;

    // valid when quantity is >= 0 and if the item exists
    valid = quantity >= 0 && DatabaseSelectHelper.getItem(itemId, context) != null;
    Log.d("quantity and item id " , Integer.toString(quantity)+ "   " + Integer.toString(itemId));
    if (valid) {
      // update the inventory
      complete = db.updateInventoryQuantity(quantity, itemId);

    } else {
      // return false to indicate there was an error in the inputs
      return false;
    }

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;
  }

  /**
   * This method updates the inventory quantity of a specific item.
   * 
   * @param accountId - the account to be updated.
   * @param active - the new status of the account.
   * @param context - the appcontext needed to access the database
   * @return - true if successful, false otherwise (invalid accountId).
   */
  public static boolean updateAccountStatus(int accountId, boolean active, Context context) {
    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);
    boolean complete = false;

    complete = db.updateAccountStatus(accountId, active);

    try {
      // try to close the connection
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return complete;
  }

  /**
   * Updates a users password in the database.
   * @param password the HASHED password of the user (not plain text!).
   * @param context the app context needed to access the database
   * @return true if update succeeded, false otherwise.
   */
  protected static boolean updateUserPassword(String password, int id,
                                              Context context) {

    DatabaseDriverAndroid db = new DatabaseDriverAndroid(context);

    Boolean isHasUpdated = db.updateUserPassword(password, id);

    // Trying to close the database
    try{
      db.close();
    } catch (Exception e){
      e.printStackTrace();
    }

    return isHasUpdated;
  }



}
