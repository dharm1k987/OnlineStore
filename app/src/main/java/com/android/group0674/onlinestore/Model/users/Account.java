package com.android.group0674.onlinestore.Model.users;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;

/**
 * Represents a customer's recent shopping history.
 * 
 * @author patelv73
 *
 */
public class Account implements Serializable {
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 7823889447499771402L;
  private int accountId = 0;
  private ShoppingCart cart = null;
  private int customerId = 0;
  private boolean accountStatus = true;
  private Context context;



  private HashMap<Item, Integer> itemMap = null;

  /**
   * Constructor for the Account.
   */
  public Account(int accountId, int customerId, Context context) {
    this.customerId = customerId;
    this.accountId = accountId;
    itemMap = new HashMap<>();
    this.context = context;

  }

  public HashMap<Item, Integer> getItemMap() {
    return this.itemMap;
  }

  /**
   * Sets the itemMap associated with the account.
   * 
   * @param itemMap the itemMap to be set
   */
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }

  /**
   * Returns the previous shopping history of the customer.
   * 
   * @return a ShoppingCart
   */
  public ShoppingCart getPreviousCart() {
    return this.cart;
  }

  /**
   * Sets the previous shopping history of the customer.
   * 
   * @param oldCart the ShoppingCart to be saved
   */
  public void saveCart(ShoppingCart oldCart) {
    this.cart = oldCart;
  }

  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }


  public void addItemToAccount(Item item, int quantity, Context context2) {
    // the item id, the quantity
    Log.d("the item id is ", Integer.toString(item.getId()) + " qua: " + Integer.toString(quantity));
    int x = DatabaseInsertHelper.insertAccountLine(this.getAccountId(), item.getId(), quantity, context2);
    Log.d("THE RECORD ID OF OF THE SALE IS " , Integer.toString(x));
  }

  public boolean isActive() {
    return this.accountStatus;
  }

  public void setAccountStatus(boolean accountStatus) {
    this.accountStatus = accountStatus;
  }

  public boolean isAccountStatus() {
    return this.accountStatus; // true means active, false means inactive
  }


}
