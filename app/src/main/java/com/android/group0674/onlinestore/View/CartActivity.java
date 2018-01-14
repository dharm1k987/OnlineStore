package com.android.group0674.onlinestore.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group0674.onlinestore.Controller.Cart.CartCheckoutOnClickController;
import com.android.group0674.onlinestore.Controller.Cart.CartContinueShopOnClickListener;
import com.android.group0674.onlinestore.Controller.Cart.CartRestoreOnClickController;
import com.android.group0674.onlinestore.Controller.User.ViewCartOnClickController;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity that shows the items in the shopping cart for a User and displays
 * other options that a User can select while shopping
 */
public class CartActivity extends AppCompatActivity implements Serializable {

    private ShoppingCart cart;
    private ShoppingCart cartPrevious;

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 7823811123516879402L;

  /**
     * Runs when this activity is called.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // get the total price and total items views
        TextView totalPrice = (TextView)findViewById(R.id.lblTotalCost);

        // get the checkout button
        Button btnCheckout = (Button) findViewById(R.id.checkoutBtn);

        // get the list view
        ListView listView = (ListView)findViewById(R.id.listCart);
        Intent intent2 = getIntent();

        cart = (ShoppingCart) this.getIntent().getSerializableExtra("cart");

       if (intent2.hasExtra("cart2")) {
            // replace it entirely
            cart = (ShoppingCart) this.getIntent().getSerializableExtra("cart2");
            cartPrevious = cart;
        }



        // create a hashmap to store the item map of the cart
        HashMap<Item, Integer> items = cart.getItemMap();
        List<String> itemHeaders = new ArrayList<>();

        // Go through each item and add it in the listview.
        for (Item item: items.keySet()) {
            itemHeaders.add(item.getName() + " \nQuantity: " + items.get(item).toString());
        }
        // display the list view
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, itemHeaders);

        listView.setAdapter(listAdapter);

        // continue shopping btn
        Button contBtn = (Button) findViewById(R.id.continueShoppingBtn);

        contBtn.setOnClickListener(new CartContinueShopOnClickListener(this));

        // check out listener
        btnCheckout.setOnClickListener(new CartCheckoutOnClickController(this, cart));

        totalPrice.setText("Total Price: (w/o tax): $" + cart.getTotal());

        // Restore the shopping cart
        Button restoreCartBtn = (Button)findViewById(R.id.restoreCartBtn);
        restoreCartBtn.setOnClickListener(new CartRestoreOnClickController(this, cart));
    }



}
