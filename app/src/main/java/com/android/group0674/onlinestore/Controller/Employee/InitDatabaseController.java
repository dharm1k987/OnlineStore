package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseDriverAndroid;
import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;

import java.math.BigDecimal;

/**
 * Created by Dharmik on November 29, 2017.
 * Controller class that serves as a controller to decide the behaviour of the initialization of
 * the database
 */
public class InitDatabaseController {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public InitDatabaseController(Context context){
        this.appContext = context;
    }

    /**
     * Set up the database on first run of the app by inserting an inventory and inserting employee
     * roles into the database.
     */
    public void setUpDatabase() {
        // Let's create the database driver
        DatabaseDriverAndroid databaseDriverAndroid = new DatabaseDriverAndroid(appContext);

        try {
            int roleId1 = DatabaseInsertHelper.insertRole("EMPLOYEE", appContext);
            int roleId2 = DatabaseInsertHelper.insertRole("CUSTOMER", appContext);

            int itemId1 = DatabaseInsertHelper.insertItem("FISHING_ROD", new BigDecimal("50.00"), appContext);
            int itemId2 = DatabaseInsertHelper.insertItem("HOCKEY_STICK", new BigDecimal("100.00"), appContext);
            int itemId3 = DatabaseInsertHelper.insertItem("SKATES", new BigDecimal("75.00"), appContext);
            int itemId4 = DatabaseInsertHelper.insertItem("RUNNING_SHOES", new BigDecimal("60.00"), appContext);
            int itemId5 = DatabaseInsertHelper.insertItem("PROTEIN_BAR", new BigDecimal("10.00"), appContext);

            int inventoryId1 = DatabaseInsertHelper.insertInventory(itemId1, 100, appContext);
            int inventoryId2 = DatabaseInsertHelper.insertInventory(itemId2, 100, appContext);
            int inventoryId3 = DatabaseInsertHelper.insertInventory(itemId3, 100, appContext);
            int inventoryId4 = DatabaseInsertHelper.insertInventory(itemId4, 100, appContext);
            int inventoryId5 = DatabaseInsertHelper.insertInventory(itemId5, 100, appContext);
            Toast.makeText(appContext, "Successfully initialized Database", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(appContext, "Failed to setup database", Toast.LENGTH_LONG).show();
        }
    }
}
