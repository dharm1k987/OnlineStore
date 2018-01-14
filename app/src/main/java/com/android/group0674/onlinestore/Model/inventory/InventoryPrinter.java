package com.android.group0674.onlinestore.Model.inventory;

import com.android.group0674.onlinestore.Model.store.ObjectPrinter;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * To print all the items in the inventory.
 * 
 * @author Harsh
 *
 */
public class InventoryPrinter implements ObjectPrinter {

  /**
   * This method will display the items in the inventory.
   */
  @Override
  public void displayItems(HashMap<Item, Integer> items) {

    for (Entry<Item, Integer> entry : items.entrySet()) {
      // get the item name, value, and print it
      Item key = entry.getKey();
      Integer value = entry.getValue();
      System.out.printf("%-22s%-22s%-22s\n", key.getName(), "(id " + key.getId() + ")", value);
    }

  }

}
