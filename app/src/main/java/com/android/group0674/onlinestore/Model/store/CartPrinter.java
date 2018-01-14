package com.android.group0674.onlinestore.Model.store;

import com.android.group0674.onlinestore.Model.inventory.Item;
import java.util.HashMap;
import java.util.Map.Entry;

public class CartPrinter implements ObjectPrinter {

  /**
   * This method will display the items in the cart.
   */
  @Override
  public void displayItems(HashMap<Item, Integer> items) {
    System.out.println("\nITEMS IN CART:");
    boolean empty = true;

    for (Entry<Item, Integer> entry : items.entrySet()) {
      empty = false;
      // get the item name, value, and print it
      Item key = entry.getKey();
      Integer value = entry.getValue();
      System.out.printf("%-22s%-22s%-22s\n", key.getName() + ":", "(id " + key.getId() + ")",
          value);
    }
    if (empty) {
      System.out.println("Empty.");
    }
    System.out.println("\n");

  }


}
