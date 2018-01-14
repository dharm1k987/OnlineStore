package com.android.group0674.onlinestore.Model.store;

import com.android.group0674.onlinestore.Model.inventory.Item;
import java.util.HashMap;

/**
 * This interface helps print the contents.
 * 
 * @author shahdha2
 *
 */
public interface ObjectPrinter {
  public void displayItems(HashMap<Item, Integer> items);
}
