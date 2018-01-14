package com.android.group0674.onlinestore.Model.inventory;

import java.util.HashMap;

/**
 * This interface is for copmaring to see what it means for items to be equal.
 * 
 * @author dharmik
 *
 */
public interface ItemComparable {

  public Item getItemInOther(HashMap<Item, Integer> hashMap, Item item);

}
