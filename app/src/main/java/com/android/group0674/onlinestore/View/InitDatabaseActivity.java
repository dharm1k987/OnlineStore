package com.android.group0674.onlinestore.View;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.group0674.onlinestore.Controller.Employee.InitDatabaseController;
import com.android.group0674.onlinestore.Model.database.DatabaseDriverAndroid;
import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.Controller.TransitionRunnable;


/**
 * Activity that serves to initialize the database while providing User with
 * a good User Interface
 */
public class InitDatabaseActivity extends AppCompatActivity {

    /**
     * When this activity is first created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_database);

        // Create a new database Controller object to set up the database
        InitDatabaseController initDatabaseController = new InitDatabaseController(this);
        initDatabaseController.setUpDatabase();

        // Creates a handler object to run this activity for 5 seconds and switch activities
        Handler handler = new Handler();
        handler.postDelayed(new TransitionRunnable(this), 5000);


    }

}
