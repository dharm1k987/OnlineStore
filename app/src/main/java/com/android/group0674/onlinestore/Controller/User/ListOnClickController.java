package com.android.group0674.onlinestore.Controller.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishantiwari on 2017-12-01.
 * Controller class that serves as a controller to decide the behaviour of the list of items in
 * shopping cart
 */
public class ListOnClickController implements AdapterView.OnItemClickListener {

    private Context appContext;
    private String quantity;
    private List<Item> items = new ArrayList<>();
    private ShoppingCart cart;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     * @param cart the shopping cart to add items to
     * @param items the list on inventory
     */
    public ListOnClickController(Context context, List<Item> items, ShoppingCart cart){
        this.appContext = context;
        this.items = items;
        this.cart = cart;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        // set the alert view
        View alertView = (LayoutInflater.from(this.appContext)).inflate(R.layout.activity_alertdialog, null);
        final EditText userId = (EditText) alertView.findViewById(R.id.userInput);
        userId.setHint("Enter new quantity");

        // get the item that the customer selected to buy
        final Item item = items.get(i);


        // Get the name of the item that the customer selected
        String itemName = String.valueOf(adapterView.getItemAtPosition(i));
        // Toast.makeText(appContext, items.get(i).getName(), Toast.LENGTH_LONG).show();
        int itemId = items.get(i).getId();
        final int inventoryQuantity = DatabaseSelectHelper.getInventoryQuantity(itemId, appContext);
        Log.d("the inventory quantitiy is ", Integer.toString(inventoryQuantity));


        // set up alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.appContext)
                .setTitle("Cart Item")
                .setMessage("Enter new quantity:")
                .setView(alertView)
                .setCancelable(true)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quantity = userId.getText().toString();
                        // if the customer entered an empty quantity, show them
                        if(quantity.length() == 0){
                            Toast.makeText(appContext, "Invalid input", Toast.LENGTH_LONG).show();
                        } else {
                            // otherwise it is a valid input, so try inserting
                            int quantityNum = Integer.parseInt(userId.getText().toString());

                            // if the quantity is less than 0 (invalid)
                            if ( quantityNum < 0 ) {
                                // failed to create account
                                Toast.makeText(appContext, "Invalid quantity", Toast.LENGTH_LONG).show();
                            } else {
                                // if the qunaitty is greater than the inventory, tell them not enough stock
                                if (inventoryQuantity < quantityNum) {
                                    Toast.makeText(appContext, "Not enough stock", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    // add item to cart
                                    cart.addItem(item, quantityNum);
                                    Toast.makeText(appContext, "Added item to cart", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new NegativeButtonListener());

        Dialog dialog = alertDialog.create();
        dialog.show();
    }

    /**
     * Class to controll the negative button listener
     */
    private class NegativeButtonListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }
}
