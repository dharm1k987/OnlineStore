package com.android.group0674.onlinestore.Controller.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.View.CustomerActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the checkout button
 */
public class CartCheckoutOnClickController implements View.OnClickListener {

    private Context appContext;
    private ShoppingCart cart;
    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param appContext
     * @param cart the shopping cart that needs to be checked out
     */
    public CartCheckoutOnClickController(Context appContext, ShoppingCart cart){
        this.appContext = appContext;
        this.cart = cart;
    }
    public void onClick(View view) {
        // create an alert dialog to ask for confirmation of checkout
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.appContext)
                .setTitle("Checkout")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // check out the shopping cart (in the database)
                        boolean status = cart.checkOut(cart, appContext);

                        // go back to the main customer activity
                        Intent intent = new Intent(appContext, CustomerActivity.class);
                        intent.putExtra("customerId",cart.getCustomer().getId());
                        appContext.startActivity(intent);
                        ((AppCompatActivity) appContext).finish();

                        try{
                            if (status) {
                                // succussccully checkout out
                                cart = new ShoppingCart( cart.getCustomer(), appContext);
                                Toast.makeText(appContext, "Successfully checked out", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(appContext, "Unable to check out", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e){
                            Toast.makeText(appContext, "Invalid Customer", Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // if the dont want to cehkout, the dismiss the alert
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.create();
        alertDialog.show();

        }


    }

