package com.android.group0674.onlinestore.Model.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.exceptions.ConnectionFailedException;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseInsertException;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.Sale;
import com.android.group0674.onlinestore.Model.store.SalesLog;
import com.android.group0674.onlinestore.Model.users.Account;
import com.android.group0674.onlinestore.Model.users.User;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class DeserializeDB {
  private Context appContext;

  public DeserializeDB(Context context){
    this.appContext = context;
  }
  public void deserialize() {
    try {
      // Initialize the serializeDB to get the absolute path of the database
      SerializeDB serializeDB = new SerializeDB(appContext);
      String databaseFileName = serializeDB.getFileName();

      FileInputStream fileIn = new FileInputStream(databaseFileName);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      
      // Let's first clean the database before we input the new information
      //Connection connection = DatabaseDriver.reInitialize();
      DatabaseDriverAndroid databaseDriverAndroid = new DatabaseDriverAndroid(appContext);
      databaseDriverAndroid.onUpgrade(databaseDriverAndroid.getReadableDatabase(),
              databaseDriverAndroid.getReadableDatabase().getVersion(),
              databaseDriverAndroid.getReadableDatabase().getVersion() + 1);

      //SerializableObject serializedItems = null;
      SerializableObject serializedItems = null;
      try {
        serializedItems = (SerializableObject) in.readObject();
      } catch (EOFException e) {
      }
      
      
      ArrayList<Object> items = serializedItems.getItemsSerialized();

      
      // Now we have the list of objects that were serialized, we have to deserialize them
      
      // Loop through the objects serialized to
      
      ArrayList<String> roleIdInfo = (ArrayList<String>) items.get(0);
      
      for(String roleName : roleIdInfo) {
        DatabaseInsertHelper.insertRole(roleName, appContext);
      }
      
      // All roles have been added to the database at this point
      
     
      ArrayList<Item> listOfItems = (ArrayList<Item>) items.get(1);
      
      for(Item item : listOfItems) {
        DatabaseInsertHelper.insertItem(item.getName(), item.getPrice(), appContext);
      }
      
      // All items have been added to the database at this point

      // Accounts have been added to the database at this point


      ArrayList<User> listOfUsers = (ArrayList<User>) items.get(5);

      // Going through the list of Users and adding them to the database
      for(User user : listOfUsers) {

        DatabaseInsertHelper.insertNewUser(user.getName(), user.getAge(), user.getAddress(),
                "Hello", appContext);

        DatabaseUpdateHelper.updateUserPassword(user.getEncryptedPassword(), user.getId(),
                appContext);

        DatabaseInsertHelper.insertUserRole(user.getId(), user.getRoleId(), appContext);
      }

      
      SalesLog allSales = (SalesLog) items.get(2);
      
      // Looping through the sales in the SalesLog
      for(Sale sale : allSales.getAllSales()) {
        int x = DatabaseInsertHelper.insertSale(sale.getUser().getId(), sale.getTotalPrice(), appContext);
        
        HashMap<Item, Integer> itemsInSale = sale.getItemMap();
        
        for(Entry<Item, Integer> item : itemsInSale.entrySet()) {
          DatabaseInsertHelper.insertItemizedSale(sale.getId(), item.getKey().getId(),
              item.getValue(), appContext);
        }
      }
      
      // Sales have been added to the database at this point including the itemizedsale
      
      
      
      HashMap<Integer, Integer> inventoryMapping = (HashMap<Integer, Integer>) items.get(3);
      
      for(Entry<Integer, Integer> inventoryEntry : inventoryMapping.entrySet()) {
        DatabaseInsertHelper.insertInventory(inventoryEntry.getKey(), inventoryEntry.getValue(),
                appContext);
      }
      
      // At this point, items have been added to the inventoryMapping
      
      
      // Getting the list of accounts stored
      ArrayList<Account> listOfAccounts = (ArrayList<Account>) items.get(4);
      
      // Now we are traversing through the accounts and adding their information to the database
      for(Account account : listOfAccounts) {
        DatabaseInsertHelper.insertAccount(account.getCustomerId(), account.isActive(), appContext);
        
        HashMap<Item, Integer> accountItems = account.getItemMap();
          
        // Looping through the items in each account and adding it to AccountSummary table
        for(Entry<Item, Integer> item : accountItems.entrySet()) {
          DatabaseInsertHelper.insertAccountLine(account.getAccountId(), item.getKey().getId(),
              item.getValue(), appContext);
        }
        
      }
      

      
      
      in.close();
      fileIn.close();
      Toast.makeText(appContext, "The database has been loaded", Toast.LENGTH_SHORT).show();
    } catch (IOException i) {
      Toast.makeText(appContext, "Please save the database first", Toast.LENGTH_SHORT).show();
      return;
    } catch (ClassNotFoundException c) {
      System.out.println("Serializable Item class not found");
      c.printStackTrace();
    } catch(DatabaseInsertException e) {
      System.out.print("Errors in inserting into the database");
    }
  }

}
