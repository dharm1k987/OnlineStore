package com.android.group0674.onlinestore.Model.users;

import com.android.group0674.onlinestore.Model.inventory.Item;

/**
 * This interface allows an employee to restock the inventory.
 * 
 * @author shahdha2
 *
 */
public interface InventoryRestockable {

  public boolean restockInventory(Item item, int quantity);

}
