package com.android.group0674.onlinestore.Controller.Employee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseUpdateHelper;
import com.android.group0674.onlinestore.Model.inventory.Item;
import com.android.group0674.onlinestore.Model.users.User;
import com.android.group0674.onlinestore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the list of inventory
 */
public class RestockListOnClickListener implements AdapterView.OnItemClickListener {
    private Context appContext;
    private String input;
    private List<Item> items;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * and takes the items that need restocking
     * @param context
     * @param items the items that need restocking
     */
    public RestockListOnClickListener(Context context, List<Item> items){
        this.appContext = context;
        this.items = items;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {

        // Create a view for list and get the item quantity that the user entered
        View itemChooser = (LayoutInflater.from(appContext)).inflate(R.layout.activity_restock_dialog, null);
        final EditText itemQuantity = (EditText) itemChooser.findViewById(R.id.restockQuantity);

        final String itemName = String.valueOf(items.get(i).getName());

        // create the alert dialog that is clicked depending on the specific item that prompts
        // the employee the wuantity they want to restock with
        AlertDialog.Builder inventoryChooser = new AlertDialog.Builder(appContext)
                .setTitle("Restock Inventory")
                .setMessage("Enter Quantity")
                .setView(itemChooser)
                .setCancelable(true)
                .setPositiveButton("Restock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        input = itemQuantity.getText().toString();
                        try{
                            // if the employee did not enter the quantity
                            if(input.length() == 0){
                                Toast.makeText(appContext, "Invalid input", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            } else {

                                int quantity = Integer.parseInt(input);
                                // otherwies valid input, so try inserting

                                // if a negative quantity is entered, make a toast informing them
                                if ( quantity < 0 ) {
                                    // failed to create account
                                    Toast.makeText(appContext, "Invalid quantity", Toast.LENGTH_LONG).show();
                                } else {
                                    // update the database with the new qunaityt for that item
                                    int itemId = DatabaseSelectHelper.getItemIdByName(itemName,
                                            appContext);

                                    DatabaseUpdateHelper.updateInventoryQuantity(quantity,
                                            itemId, appContext);

                                    Toast.makeText(appContext, "Successfully updated inventory",
                                            Toast.LENGTH_SHORT).show();
                                    ((AppCompatActivity) appContext).finish();
                                    // restart the activity incase the employee wants to restock again
                                    ((AppCompatActivity) appContext).startActivity(((AppCompatActivity) appContext).getIntent());
                                }
                            }

                        } catch (Exception e){
                            Toast.makeText(appContext, "Problem Restocking Item",
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }


                    }
                })
                // if the employee selects cancel, then dismiss alert dialog
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        Dialog dialog = inventoryChooser.create();
        dialog.show();
    }
}
