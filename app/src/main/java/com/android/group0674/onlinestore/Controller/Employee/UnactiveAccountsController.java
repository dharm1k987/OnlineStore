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
 * Controller class that serves as a controller to decide the behaviour of the unactive accounts button
 */
public class UnactiveAccountsController implements View.OnClickListener {
    private Context appContext;
    private int customerId;

    public UnactiveAccountsController(Context context, int customerId) {
        this.appContext = context;
        this.customerId = customerId;
    }

    public void onClick(View view) {
        // get the button and change its color to red so we know its chosen
        Button activeBtn = (Button) ((AppCompatActivity) appContext).findViewById(R.id.viewActiveBtn);
        Button unactiveBtn = (Button) ((AppCompatActivity) appContext).findViewById(R.id.viewUnactiveBtn);

        unactiveBtn.getBackground().setColorFilter(ContextCompat.getColor(appContext, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        activeBtn.getBackground().setColorFilter(android.R.drawable.btn_default, PorterDuff.Mode.MULTIPLY);


        // get the list view.
        // create a listview
        ListView listView = (ListView) ((AppCompatActivity) appContext).findViewById(R.id.listAcccounts);
        List<String> itemHeaders = new ArrayList<>();

        // get list of inactive from database
        List<Account> inactiveAccounts = DatabaseSelectHelper.getUserInactiveAccounts(customerId, appContext);

        // get the text id
        TextView textView = (TextView) ((AppCompatActivity) appContext).findViewById(R.id.listOfAccountsTv);
        // set it to say "inACTIVE ACCOUNTS"
        textView.setText("INACTIVE ACCOUNTS: ");

        for (Account account : inactiveAccounts) {
            String result = "";
            result += "Account ID : " + account.getAccountId();
            itemHeaders.add(result);
        }

        // IF NONE ARE THERE
        if (itemHeaders.size() == 0) {
            itemHeaders.add("NONE FOUND");
        }
        // set the list adapter to set the values of the list
        ListAdapter listAdapter = new ArrayAdapter<String>(appContext, android.R.layout.simple_expandable_list_item_1, itemHeaders);
        listView.setAdapter(listAdapter);
    }

}
