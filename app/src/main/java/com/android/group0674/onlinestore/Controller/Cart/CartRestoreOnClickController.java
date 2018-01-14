package com.android.group0674.onlinestore.Controller.Cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.group0674.onlinestore.Controller.User.ViewCartOnClickController;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.store.ShoppingCart;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.View.CartActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the resstore cart button
 */
public class CartRestoreOnClickController implements View.OnClickListener{

    private Context appContext;
    private static  ShoppingCart shoppingCart;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     * @param shoppingCart the shopping cart to restore
     */
    public CartRestoreOnClickController(Context context, ShoppingCart shoppingCart){
        this.appContext = context;
        this.shoppingCart = shoppingCart;
    }

    public void onClick(View view) {
        // Using the customer id, get the account id


        // Just get the previous shopping cart from the database
        //DatabaseSelectHelper.restoreShoppingCart()

        // Restore shopping cart, if there is an account
        try {
            shoppingCart = DatabaseSelectHelper.restoreShoppingCart(
                    (shoppingCart.getCustomer()).getAccount().getAccountId() - 1,
                    shoppingCart.getCustomer(), appContext);

            Toast.makeText(appContext, "Restored shopping cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(appContext, CartActivity.class);
            // put the cart
            intent.putExtra("cart2", shoppingCart);

            ((AppCompatActivity) appContext).finish();
            appContext.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(appContext, "No previous shopping history or account found", Toast.LENGTH_SHORT).show();
        }
    }

}
