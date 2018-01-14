package com.android.group0674.onlinestore.Model.database;

import com.android.group0674.onlinestore.Model.database.DatabaseDriver;
import java.sql.Connection;

public class DatabaseDriverHelper extends DatabaseDriver {

  protected static Connection connectOrCreateDataBase() {
    return DatabaseDriver.connectOrCreateDataBase();
  }

}
