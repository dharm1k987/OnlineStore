package com.android.group0674.onlinestore.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.group0674.onlinestore.Controller.User.ListOnClickController;
import com.android.group0674.onlinestore.Controller.User.UserLogOutOnClickController;
import com.android.group0674.onlinestore.Controller.User.ViewCartOnClickController;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Activity that serves to provide user with options to select in the online store
 */
public class CustomerActivity extends AppCompatActivity implements Serializable {

    private static final long serialVersionUID = 1263815381099966668L;
    private ShoppingCart cart;

    /**
     * Runs when this activity opens.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // get the customerId from intent
        Intent myIntent = getIntent(); // gets the previously created intent
        int customerId = myIntent.getIntExtra("customerId", 0);

        // get the list view
        ListView listView = (ListView) findViewById(R.id.list);
        Customer customer = (Customer) DatabaseSelectHelper.getUserDetails(customerId, this);

        // create a new shopping cart object for this customer
        try {
            cart = new ShoppingCart(customer, this);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }

        // get the items in the inventory
        List<Item> items = DatabaseSelectHelper.getAllItems(this);
        List<String> itemHeaders = new ArrayList<>();

        // add the item info in the list view
        for (Item item: items) {
            itemHeaders.add(item.getName() + " \nPrice: $" + item.getPrice().toString());
        }
        // display this list view
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, itemHeaders);
        listView.setAdapter(listAdapter);

        // make the list view clickable
        listView.setOnItemClickListener(new ListOnClickController(this, items, cart));

        // set on click listener for the logout button
        Button btnLogout = (Button)findViewById(R.id.logOutBtn);
        btnLogout.setOnClickListener(new UserLogOutOnClickController(this));

        // view cart on click listener
        Button btnViewCart = (Button) findViewById(R.id.viewCartBtn);
        btnViewCart.setOnClickListener(new ViewCartOnClickController(this, cart));

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
