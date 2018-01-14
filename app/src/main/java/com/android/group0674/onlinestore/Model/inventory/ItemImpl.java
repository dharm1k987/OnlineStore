package com.android.group0674.onlinestore.Model.inventory;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemImpl implements Item, Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 8688416443304394814L;
  private int id;
  private String name;
  private BigDecimal price;

  /**
   * The constructor for ItemImpl.
   * 
   * @param id - the id of the item.
   * @param name - the name of the item.
   * @param price - the price the item is selling for.
   */
  public ItemImpl(int id, String name, BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  /**
   * This method will return the id of the item.
   * 
   * @return - the id of the item.
   */
  @Override
  public int getId() {
    return this.id;
  }

  /**
   * This method will set the id of the item.
   * 
   * @param id - the id to be set.
   */
  @Override
  public void setId(int id) {
    this.id = id;

  }

  /**
   * This method will return the name of the item.
   * 
   * @return - the name of the item.
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * This method will set the name of the item.
   * 
   * @param name - the name to be set.
   */
  @Override
  public void setName(String name) {
    this.name = name;

  }

  /**
   * This method will return the price of the item.
   * 
   * @return - the price of the item.
   */
  @Override
  public BigDecimal getPrice() {
    return this.price;
  }

  /**
   * This method will set the price of the item.
   * 
   * @param price - the price to be set.
   */
  @Override
  public void setPrice(BigDecimal price) {
    this.price = price;

  }

}
