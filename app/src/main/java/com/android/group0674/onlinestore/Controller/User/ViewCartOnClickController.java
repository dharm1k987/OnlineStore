package com.android.group0674.onlinestore.Controller.User;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.group0674.onlinestore.Controller.Cart.CartRestoreOnClickController;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseUpdateHelper;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.View.CartActivity;

import java.io.Serializable;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the view cart button
 */
public class ViewCartOnClickController implements View.OnClickListener, Serializable {

    private Context appContext;
    private ShoppingCart cart;


    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     * @param cart the cart to view
     */
    public ViewCartOnClickController(Context context, ShoppingCart cart) {
        this.appContext = context;
        this.cart = cart;
    }

    @Override
    public void onClick(View view) {
        // move to view cart acitivty
          Intent intent = new Intent(appContext, CartActivity.class);
        intent.putExtra("cart", cart);
        appContext.startActivity(intent);
    }

}
