package com.android.group0674.onlinestore.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.group0674.onlinestore.Controller.User.ListOnClickController;
import com.android.group0674.onlinestore.Controller.User.ViewBooksListOnClickController;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.Sale;
import com.android.group0674.onlinestore.Model.store.SalesLog;
import com.android.group0674.onlinestore.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows all the accounts when this activity runs
 */
public class ViewBooksActivity extends AppCompatActivity {

    /**
     * Runs when this activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        // create a listview
        ListView listView = (ListView) findViewById(R.id.listBooks);
        List<String> itemHeaders = new ArrayList<>();

        // get salesLog from the database;
        SalesLog salesLog = DatabaseSelectHelper.getSales(this);
        // get the itemized salesLog
        DatabaseSelectHelper.getItemizedSales(salesLog, this);

        // create a totalPrice variable that will keep track of the total price of all the sales
        double totalPrice = 0.00;
        // create a hashMap that will keep track of the items sold and their quantities, in totality
        HashMap<Item, Integer> totalItemsSold = new HashMap<Item, Integer>();




        // Go through each sale
        for (Sale sale : salesLog.getAllSales()) {
            String to_add = "";


            // print the name of the customer associated with the sale
            String name = sale.getUser().getName();

            to_add += "Customer: " + name;
            // print the purchase number i.e the saleId
            to_add += "\nPurchase Number: " + sale.getId();

            // print the purchase price
            to_add += "\nTotal Purchase Price: $" + sale.getTotalPrice();

            // add the total price to the sum of total price
            totalPrice += sale.getTotalPrice().doubleValue();
            // print the itemized breakdown
            to_add += "\n\nItemized Breakdown:\n";

            // get the itemmized breakdown in a hashmap
            HashMap<Item, Integer> itemMap = sale.getItemMap();

            // go through each item and print its name and its quantity
            for (Map.Entry<Item, Integer> entry : itemMap.entrySet()) {
                // get the item name, value, and print it
                Item key = entry.getKey();
                Integer value = entry.getValue();
                to_add += key.getName() + "  :  " + value+"\n";
                // add the quantity of the items sold to the total item list.

                // if the item already exists in the totalItemSold ,
                // then simply add to its quantity
                Item keyInOther = getItemInOther(totalItemsSold, key);
                if (keyInOther != null) {
                    // add the previous quantity of the item to the total quantity.
                    int prevq = totalItemsSold.get(keyInOther);
                    totalItemsSold.put(keyInOther, prevq + value);
                } else {
                    // add to the total items sold
                    totalItemsSold.put(key, value);
                }
            }
            // now add it to the itemHeaders
            itemHeaders.add(to_add);

        }

        for (Map.Entry<Item, Integer> entry : totalItemsSold.entrySet()) {
            // get the Item name, value and print it
            Item key = entry.getKey();
            Integer value = entry.getValue();
            // add to the item headers
            itemHeaders.add("Number of  " + key.getName() + "  sold : " + value);
        }

        // print the total price
        BigDecimal finalPrice = BigDecimal.valueOf(totalPrice).add(new BigDecimal("0.00"));
        finalPrice = finalPrice.setScale(2, BigDecimal.ROUND_UP);
        // add the total price
        itemHeaders.add("TOTAL SALES: $" + finalPrice);

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, itemHeaders);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new ViewBooksListOnClickController(this, itemHeaders));
    }

    /**
     * Gets the items from the hashmap
     * @param hashMap contains all items
     * @param item item you want to find
     * @return
     */
    public Item getItemInOther(HashMap<Item, Integer> hashMap, Item item) {
        // loop through hasmhap
        Item toReturn = null;
        for (Map.Entry<Item, Integer> entry : hashMap.entrySet()) {

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
}
