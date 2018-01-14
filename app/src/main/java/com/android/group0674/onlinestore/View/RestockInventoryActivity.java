package com.android.group0674.onlinestore.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.group0674.onlinestore.Controller.Employee.RestockListOnClickListener;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity allows the employee to restock the inventory
 */
public class RestockInventoryActivity extends AppCompatActivity {

    /**
     * when this activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_inventory);

        // get the title
        TextView inventoryTitle = (TextView)findViewById(R.id.inventoryTitle);
        // get the list view
        ListView listView = (ListView)findViewById(R.id.itemsList);

        // get the items in the inventory
        List<Item> items = DatabaseSelectHelper.getAllItems(this);
        List<String> itemHeaders = new ArrayList<>();

        // go through each item and add it to the list view.
        for (Item item: items) {
            int itemQuantity = DatabaseSelectHelper.getInventoryQuantity(item.getId(),
                    this);

            itemHeaders.add(item.getName() + "\nCurrent Quantity : " + itemQuantity);
        }

        // Show the items in the list view
        ListAdapter listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, itemHeaders);

        listView.setAdapter(listAdapter);

        // Make the list view clickable
        listView.setOnItemClickListener(new RestockListOnClickListener(this, items));
    }
}
