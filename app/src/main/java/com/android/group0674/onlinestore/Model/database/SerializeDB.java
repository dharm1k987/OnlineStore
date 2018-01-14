package com.android.group0674.onlinestore.Model.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.inventory.Inventory;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.Sale;
import com.android.group0674.onlinestore.Model.store.SalesLog;
import com.android.group0674.onlinestore.Model.users.Account;
import com.android.group0674.onlinestore.Model.users.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SerializeDB {

    private Context appContext;

    public SerializeDB(Context context){
        this.appContext = context;
    }

    public String getFileName() {
        File file = new File(this.appContext.getExternalFilesDir(null), "database_copy.ser");
        return file.getAbsolutePath();
    }

    public void serialize() {
        try {
        // get the file location

        File file = new File(appContext.getExternalFilesDir(null), "database_copy.ser");

        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        // TABLES
        // USERS -> id, name, age, address [done] --- listOfUsers
        // USERROLE -> userid, roleid [done] -----
        // USERPW -> userid, password [done] -------
        // SALES -> id (saleid), userid, totalprice [done] ---- allSales
        // ROLES -> id, name [done] ------ roleIdInfo
        // ITEMS -> id, name, price [done] ----- listOfItems
        // ITEMIZEDSALES -> saleid, itemid, quantity [-----> mutated, called through saleslog]
        // INVENTORY -> itemid, quantity [done] ------- inventoryMapping
        // ACCOUNTSUMMARY -> acctid, itemid, quantity [done]
        // ACCOUNT -> id, userid, active [done] ----- listOfAccounts


        // get a list of all the accounts, active and inactive
      
        ArrayList<Account> listOfAccounts = new ArrayList<>();

        // get a list of all the users
        ArrayList<User> listOfUsers = (ArrayList<User>) DatabaseSelectHelper.getUsersDetails(appContext);

      // loop through each of the users, and add their info
      for (User user : listOfUsers) {

        // Get the user password and user's role id and save it inside User
        String password = DatabaseSelectHelper.getPassword(user.getId(), appContext);

        user.setEncryptedPassword(password);

        int roleId = DatabaseSelectHelper.getUserRoleId(user.getId(), appContext);

        user.setRoleId(roleId);
        
        // Now getting all the accounts for this user
        ArrayList<Account> listOfInactiveAccounts = (ArrayList<Account>) DatabaseSelectHelper.
            getUserInactiveAccounts(user.getId(), appContext);
        
        ArrayList<Account> accountsForUser =
            (ArrayList<Account>) DatabaseSelectHelper.getUserAccounts(user.getId(), appContext);

        // Once we get the accounts, we traverse them and try to add account details
        for (Account account : accountsForUser) {
          // here we will get the account details and set the updated account in listOfAccounts
          Account updatedAccount =
              DatabaseSelectHelper.getAccountDetails(account.getAccountId(), user.getId(), appContext);
          
          for (Account account2 : listOfInactiveAccounts) {

            if (account2.getAccountId() == account.getAccountId()) {

              updatedAccount.setAccountStatus(false);
            }
            

          }
          
          // get a list of inactive, if its 
          listOfAccounts.add(updatedAccount);

        }


      }


      // At this point, we have dealt with users and accounts tables

      // create an arraylist that gets the role names
      ArrayList<String> roleIdInfo = new ArrayList<>();

      // get a list of all the users
      ArrayList<Integer> listOfRoles = (ArrayList<Integer>) DatabaseSelectHelper.getRoleIds(appContext);
      for (Integer roleId : listOfRoles) {
        roleIdInfo.add(DatabaseSelectHelper.getRoleName(roleId, appContext));


      }

      // At this point we are done with the Roles table


      // create an arraylist of items for storing information about items table

      // get a list of all the items
      ArrayList<Item> listOfItems = (ArrayList<Item>) DatabaseSelectHelper.getAllItems(appContext);


      // At this point, we are done with the Items table

      // Sales

      SalesLog allSales = DatabaseSelectHelper.getSales(appContext);
      DatabaseSelectHelper.getItemizedSales(allSales, appContext);
            Log.d("the number of sales are " , Integer.toString(allSales.getAllSales().size()));
 ;

      // INVENTORY
      Inventory inventory = DatabaseSelectHelper.getInventory(appContext);
      // inventory mapping
      HashMap<Integer, Integer> inventoryMapping = new HashMap<>();
      for (Entry<Item, Integer> entry : inventory.getItemMap().entrySet()) {
        Item key = entry.getKey();
        Integer value = entry.getValue();
        inventoryMapping.put(key.getId(), value);
      }

      ArrayList<Object> combination = new ArrayList<>();
      combination.add(roleIdInfo);
      combination.add(listOfItems);
      combination.add(allSales);
      combination.add(inventoryMapping);
      combination.add(listOfAccounts);
      combination.add(listOfUsers);

      // Create a Serializable Object instance
      SerializableObject object = new SerializableObject(combination);
      
      out.writeObject(object);
      out.close();
      fileOut.close();
      Toast.makeText(appContext, "The database has been saved", Toast.LENGTH_SHORT).show();
    } catch (IOException i) {
      i.printStackTrace();
    }
  }


}
