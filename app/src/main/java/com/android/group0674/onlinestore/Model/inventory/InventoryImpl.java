package com.android.group0674.onlinestore.Model.inventory;

import java.util.HashMap;

public class InventoryImpl implements Inventory, InventoryUpdatable {

  private HashMap<Item, Integer> itemMap = new HashMap<>();
  private int total = 0;
  private InventoryPrinter inventoryPrinter = null;

  /**
   * This method will return the item map associated with the inventory.
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    return this.itemMap;
  }

  /**
   * This method will set the item map of the inventory.
   * 
   * @param itemMap - the item map we want to set.
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }

  /**
   * This method will update the inventory map with the given item and value.
   * 
   * @param item - the item we want to update the quantity of.
   * @param value - the new value of it.
   */
  @Override
  public void updateMap(Item item, Integer value) {
    itemMap.put(item, value);
  }

  /**
   * This method will return the total number of items in the inventory.
   * 
   * @return - the total number of items in the inventory.
   */
  @Override
  public int getTotalItems() {
    return this.total;
  }

  /**
   * This method will set the total number of items in the inventory.
   * 
   * @param total - the total number of items.
   */
  @Override
  public void setTotalItems(int total) {
    this.total = this.total + total;
  }

  /**
   * This method will print out a representation of the inventory.
   */
  @Override
  public void displayInventoryMapping() {
    this.inventoryPrinter = new InventoryPrinter();
    this.inventoryPrinter.displayItems(itemMap);

  }

}
