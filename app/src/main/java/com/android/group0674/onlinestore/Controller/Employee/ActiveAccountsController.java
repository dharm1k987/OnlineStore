package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.users.Account;
import com.android.group0674.onlinestore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the view active accounts
 * button
 */
public class ActiveAccountsController implements View.OnClickListener {
    private Context appContext;
    private int customerId;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * and takes in the customer ID of the accounts we want.
     * @param context
     */
    public ActiveAccountsController(Context context, int customerId) {
        this.appContext = context;
        this.customerId = customerId;
    }

    @Override
    public void onClick(View view) {
        // get the button and change its color to red so we know its chosen
        Button activeBtn = (Button) ((AppCompatActivity) appContext).findViewById(R.id.viewActiveBtn);
        Button unactiveBtn = (Button) ((AppCompatActivity) appContext).findViewById(R.id.viewUnactiveBtn);

        activeBtn.getBackground().setColorFilter(ContextCompat.getColor(appContext, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        unactiveBtn.getBackground().setColorFilter(android.R.drawable.btn_default, PorterDuff.Mode.MULTIPLY);
        // get the list view.
        // create a listview
        ListView listView = (ListView) ((AppCompatActivity) appContext).findViewById(R.id.listAcccounts);
        List<String> itemHeaders = new ArrayList<>();

        // get the text id
        TextView textView = (TextView) ((AppCompatActivity) appContext).findViewById(R.id.listOfAccountsTv);
        // set it to say "ACTIVE ACCOUNTS"
        textView.setText("ACTIVE ACCOUNTS: ");


        // get list of inactive from database and add them to the contents of the list.
        List<Account> activeAccounts = DatabaseSelectHelper.getUserActiveAccounts(customerId, appContext);

        for (Account account : activeAccounts) {
            String result = "";
            result += "Account ID : " + account.getAccountId();
            itemHeaders.add(result);
        }
        // IF NONE ARE THERE
        if (itemHeaders.size() == 0) {
            itemHeaders.add("NO ACCOUNTS");
        }
        // create and set the list adapter to change the values in the list.
        ListAdapter listAdapter = new ArrayAdapter<String>(appContext, android.R.layout.simple_expandable_list_item_1, itemHeaders);
        listView.setAdapter(listAdapter);


    }

}
