package com.android.group0674.onlinestore.Model.inventory;

import java.util.HashMap;

public interface Inventory {

  public HashMap<Item, Integer> getItemMap();

  public void setItemMap(HashMap<Item, Integer> itemMap);

  public int getTotalItems();

  public void setTotalItems(int total);

  public void displayInventoryMapping();
}
