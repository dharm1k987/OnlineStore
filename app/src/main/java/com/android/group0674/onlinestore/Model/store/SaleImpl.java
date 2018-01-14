package com.android.group0674.onlinestore.Model.store;

import android.content.Context;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.users.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * This is the class that implements the Sale interface.
 * 
 * @author dharmik
 *
 */
public class SaleImpl implements Sale, Serializable {
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -5555025807513655094L;
  // comment added for testing
  private int id;
  private User user;
  private BigDecimal totalPrice;
  private HashMap<Item, Integer> itemMap = new HashMap<>();
  private Context context;

  /**
   * The constructor for the SaleImpl.
   * 
   * @param id - the id of the sale.
   * @param userId - the id of the user who bought something.
   * @param totalPrice - the total price amount of the sale.
   */
  public SaleImpl(int id, int userId, BigDecimal totalPrice, Context context) {
    this.id = id;
    this.totalPrice = totalPrice;
    // set the user based off the id
    this.setUser(DatabaseSelectHelper.getUserDetails(userId, context));
  }

  /**
   * This method will return the id of the sale.
   * 
   * @return id - the id of the sale.
   */
  @Override
  public int getId() {
    return this.id;
  }

  /**
   * This method will set the id of the sale.
   * 
   * @param id - the id to be set.
   */
  @Override
  public void setId(int id) {
    this.id = id;

  }

  /**
   * This method will return the user associated with the sale.
   * 
   * @return - the user associated with the sale.
   */
  @Override
  public User getUser() {
    return this.user;
  }

  /**
   * This method will set the user associated with the sale.
   * 
   * @param user - the user to be set.
   */
  @Override
  public void setUser(User user) {
    this.user = user;

  }

  /**
   * This method will return the total price of the sale.
   * 
   * @return - the total price of the sale.
   */
  @Override
  public BigDecimal getTotalPrice() {
    return this.totalPrice;
  }

  /**
   * This method will set the total price of the sale.
   * 
   * @param totalPrice - the total price of the sale.
   */
  @Override
  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;

  }

  /**
   * This method will return the item map associated with this sale.
   * 
   * @return - the item map associated with this sale.
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    return this.itemMap;
  }

  /**
   * This method will set the item map associated with this sale.
   * 
   * @param itemMap - the item map associated with this sale.
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }



}
