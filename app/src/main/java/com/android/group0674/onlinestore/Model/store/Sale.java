package com.android.group0674.onlinestore.Model.store;

import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.users.User;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * This is the interface for Sale objects.
 * 
 * @author dharmik
 *
 */
public interface Sale {

  public int getId();

  public void setId(int id);

  public User getUser();

  public void setUser(User user);

  public BigDecimal getTotalPrice();

  public void setTotalPrice(BigDecimal price);

  public HashMap<Item, Integer> getItemMap();

  public void setItemMap(HashMap<Item, Integer> itemMap);

}
