package com.android.group0674.onlinestore.Model.store;

import android.content.Context;
import android.util.Log;

import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseUpdateHelper;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseInsertException;
import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;
import com.android.group0674.onlinestore.Model.inventory.Inventory;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.inventory.ItemComparable;
import com.android.group0674.onlinestore.Model.users.Customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * This is the class that the customer will use to purchase items from the inventory.
 * 
 * @author dharmik
 *
 */
public class ShoppingCart implements ItemComparable, Serializable {

  private static final long serialVersionUID = 1399854297580241622L;
  private HashMap<Item, Integer> items = new HashMap<>();
  private Customer customer = null;
  private BigDecimal total = new BigDecimal("0.00");
  private static final BigDecimal TAXRATE = new BigDecimal("1.13");
  private Sale saleMade = null;
  private CartPrinter cartPrinter = null;

  private Context context;

  /**
   * This method is the constructor of the ShoppingCart, and will set an authenticated customer.
   * 
   * @param customer - the customer to be set (only will be set if authenticated).
   * @throws InvalidArgumentException - if customer is not logged in.
   */
  public ShoppingCart(Customer customer, Context context) throws InvalidArgumentException {
    // CHECK AUTHENTICATION
    this.context = context;

      this.customer = customer;

  }

  /**
   * This method will add a quantity of an item to their shopping cart.
   * 
   * @param item - the item we want to add.
   * @param quantity - the quantity we want to add.
   */
  public void addItem(Item item, int quantity) {
    Log.d("asjkldfhakjsdhflkjasdf","lakjdsfl;kasdjfl;kadjsf");
    if (item == null || quantity < 0) {
      System.out.println("Invalid item id or quantity.");
    } else {
      // put in our hashmap and update total
      // check if already in cart
      Item alreadyInCart = getItemInOther(this.items, item);

      if (alreadyInCart == null) {
        // adding first time
        this.items.put(item, quantity);

        this.total = this.total.add(item.getPrice().multiply(new BigDecimal(quantity)));

      } else {
        // adding again
        this.total = this.total.subtract(item.getPrice().multiply(
                new BigDecimal(this.items.get(alreadyInCart))));
        this.items.put(alreadyInCart, quantity);

        this.total = this.total.add(item.getPrice().multiply(new BigDecimal(quantity)));
      }

    }



  }

  /**
   * This method will remove a quantity of an item in their shopping cart.
   * 
   * @param item - the item they want to remove a quantity of.
   * @param quantity - how much they want to remove.
   */
  public void removeItem(Item item, int quantity) {

    // if item is null, this means it doesn't exist in the inventory
    if (item == null) {
      System.out.println("This item does not exist in the inventory.");
    } else if (quantity < 0) {
      System.out.println("Quantity to remove must be > 0");
    } else {
      // get the item in the items hashmap
      Item itemInHashmap = getItemInOther(items, item);
      if (itemInHashmap == null) {
        // this means it doesn't exist in the shopping cart
        System.out.println("This item does not exist in the shopping cart.");
        return;
      }
      // otherwise get the quantity of this item that's currently in the shopping cart
      int currentQuantity = this.items.get(itemInHashmap);
      // if we want to remove the entire quantity
      if ((currentQuantity - quantity) <= 0) {
        // remove entirely and update total by subtracting what we removed
        items.remove(getItemInOther(items, item));
        this.total = this.total.subtract(item.getPrice().multiply(new BigDecimal(currentQuantity)));
      } else {
        // otherwise, our shopping cart should now contain the different, and update total properly
        items.put(getItemInOther(items, item), currentQuantity - quantity);
        this.total = this.total.subtract(item.getPrice().multiply(new BigDecimal(quantity)));
      }
    }

  }

  /**
   * This method will return a list of Items that the customer is buying.
   * 
   * @return - the list of items that customer is buying.
   */
  public List<Item> getItems() {
    // list of just the items (which are the keys in the items hashmap)
    List<Item> itemsInShoppingCart = new ArrayList<>(this.items.keySet());
    return itemsInShoppingCart;
  }

  /**
   * This method will return the customer that is shopping.
   * 
   * @return - the customer that is shiopping.
   */
  public Customer getCustomer() {
    return this.customer;
  }

  /**
   * This method will return the total amount the customer must pay so far.
   * 
   * @return - the total amount.
   */
  public BigDecimal getTotal() {
    return this.total;
  }

  /**
   * This method will return the tax rate.
   * 
   * @return - the tax rate, a constant value of 1.13.
   */
  public BigDecimal getTaxRate() {
    return ShoppingCart.TAXRATE;
  }



  /**
   * This method will check out the customers shopping cart.
   * 
   * @param shoppingCart - the shopping cart to checkout all the items.
   * @return - true if successful, false otherwise (not enough items).
   */
  public boolean checkOut(ShoppingCart shoppingCart, Context context2) {
    // make sure we have a customer
    if (this.getCustomer() != null) {
      // calculate total after tax
      BigDecimal totalAfterTax = this.getTotal().multiply(TAXRATE);
      // round it up to 2 decimal places
      totalAfterTax = totalAfterTax.setScale(2, BigDecimal.ROUND_HALF_UP);
      // get the inventory from the database
      Inventory inventory = DatabaseSelectHelper.getInventory(context2);

      // check if inventory has enough items
      for (Item item : inventory.getItemMap().keySet()) {
        // if we are buying this item
        if (items.containsKey(getItemInOther(items, item))) {
          // check to see if our inventory quantity < what we want to purchase, that means error
          if ((inventory.getItemMap().get(item)) < items.get(getItemInOther(items, item))) {
            return false;
          }
        }
      }

      // there is enough, so remove from inventory
      // but first make the sale
      // there is enough, so remove from inventory
      // but first make the sale
      int saleId;
      try {
        saleId = DatabaseInsertHelper.insertSale(this.customer.getId(), totalAfterTax, context2);
        // loop through our items
        // if the customer has an account
        int customerAccountId = DatabaseSelectHelper.checkIfAccountExists(this.customer.getId(),context2);
        Log.d("CHECKING TO SEE IF THE CUSTOMER ACCOUNT EXISTS, ID, EXISTS: ",
                Integer.toString(this.customer.getId()) + "  " + Boolean.toString(customerAccountId != -1));
        for (Item item : items.keySet()) {
          // update the inventory in the database
          DatabaseUpdateHelper.updateInventoryQuantity(
                  inventory.getItemMap().get(getItemInOther(inventory.getItemMap(), item))
                          - items.get(item),
                  item.getId(), context2);
          // create the correct itemized sale
          DatabaseInsertHelper.insertItemizedSale(saleId, item.getId(), items.get(item), context2);
          if (customerAccountId != -1) {
            Log.d("the sale id was :  , the customer has an account :  ", Integer.toString(saleId) + "  " +
            Boolean.toString(customer.getAccount() != null));
            //Log.d("THE ACC EXIST ON DB", Boolean.toString(DatabaseSelectHelper.checkIfAccountExists(customerAccountId, context2) != -1));
            // they have an account
            this.customer.getAccount().addItemToAccount(item, items.get(item), context2);
            Log.d("the customerh as id, i will add item id: , with quantity : ", Integer.toString(customerAccountId) + " " +
            (item.toString() + "   " + Integer.toString(items.get(item))));
            DatabaseUpdateHelper.updateAccountStatus(customer.getAccount().getAccountId(), false, context2);
            this.customer.getAccount().setAccountStatus(false);
            //DatabaseUpdateHelper.updateAccountStatus(this.customer.getAccount().getAccountId(), false, context2);
          }
        }
        // clear our cart
        this.clearCart();
        // everything was successful, set the sale so we can add to SalesLog from SalesApplication
        setInSaleLog(DatabaseSelectHelper.getSaleById(saleId, context2));
        // set the total
        this.total = totalAfterTax;

        // now we create a NEW account for those who HAVE an account
        if (customerAccountId != -1) {
          int newAcc = DatabaseInsertHelper.insertAccount(this.customer.getId(), true, context2);
          Log.d("inserted new acc for customer, the id is ", Integer.toString(newAcc));
        }


        if (customer.getAccount() != null) {
          customer.getAccount().setAccountId(customer.getAccount().getAccountId() + 1);
          Log.d("i have set the new customer acc id, it should be " , Integer.toString(customer.getAccount().getAccountId()));
        }
        // customer.getAccount().setAccountId(customer.getAccount().getAccountId() + 1);
        return true;

      } catch (Exception e) {
        // if any exception occurred in inserting, we return false.
        return false;
      }
    }
    return false;

  }

  /**
   * This method will set the sale made in the salesLog.
   * 
   * @param sale - the sale we made.
   */
  private void setInSaleLog(Sale sale) {
    this.saleMade = sale;

  }

  /**
   * This method will return the sale that was completed by the customer.
   * 
   * @return - the sale made by the user.
   */
  public Sale getSaleMade() {
    return this.saleMade;
  }



  /**
   * This method will clear the shopping cart of the customer.
   */
  public void clearCart() {
    items.clear();
    // no total now
    this.total = new BigDecimal("0.00");
  }

  /**
   * This method will display the items in the shopping cart in a table layout.
   */
  public void displayItemsInCart() {
    this.cartPrinter = new CartPrinter();
    this.cartPrinter.displayItems(items);
  }

  /**
   * This method will return the Item "equivalent" in the hashmap given, based off its id.
   * 
   * @param hashMap - the hashmap we want to search for an item "equivalent".
   * @param item - the item we want to look for, whose id we will search for in the hashmap.
   * @return - the Item in the hashmap ("equivalent"), or null otherwise (invalid item).
   */
  @Override
  public Item getItemInOther(HashMap<Item, Integer> hashMap, Item item) {
    // loop through hasmhap
    Item toReturn = null;
    for (Entry<Item, Integer> entry : hashMap.entrySet()) {

      // the key is the item
      Item key = entry.getKey();
      // if both ids match, we found the equivalent
      if (key.getId() == item.getId()) {
        return key;
      }
    }
    // otherwise here it was not found, which means item was not found in the hashmap.
    return toReturn;
  }

  public HashMap<Item, Integer> getItemMap() {
    return this.items;
  }

  public void addItemMap(HashMap<Item, Integer> itemMap){
    for (Entry<Item, Integer> entry : itemMap.entrySet()) {
      // get the item name, value, and print it
      Item key = entry.getKey();
      Integer value = entry.getValue();
      addItem(key, value);
    }
  }

}
