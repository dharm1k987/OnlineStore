package com.android.group0674.onlinestore.Controller.User;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.android.group0674.onlinestore.R;

import java.util.List;

/**
 * Created by ishantiwari on 2017-12-01.
 * Controller class that serves as a controller to decide the behaviour of the list of sales
 */
public class ViewBooksListOnClickController implements AdapterView.OnItemClickListener {

    private Context appContext;
    private List<String> itemHeaders;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     * @param itemHeaders the sales in string form
     */
    public ViewBooksListOnClickController(Context context, List<String> itemHeaders){
        this.appContext = context;
        this.itemHeaders = itemHeaders;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        // create an alert dialog to show them extra details about a sale (the itemMap)
        View alertView = (LayoutInflater.from(this.appContext)).inflate(R.layout.activity_alertdialog, null);
        String message = this.itemHeaders.get(i).toString();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.appContext)
                .setTitle("Sale Details")
                //.setView(alertView)
                .setMessage(message)
                .setCancelable(true);

        alertDialog.create();
        alertDialog.show();
    }
}
