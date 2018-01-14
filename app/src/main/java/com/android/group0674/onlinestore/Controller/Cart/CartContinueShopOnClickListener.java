package com.android.group0674.onlinestore.Controller.Cart;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the continue shopping button
 */
public class CartContinueShopOnClickListener implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public CartContinueShopOnClickListener(Context context) {
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // go back
        ((AppCompatActivity) appContext).onBackPressed();

    }
}
