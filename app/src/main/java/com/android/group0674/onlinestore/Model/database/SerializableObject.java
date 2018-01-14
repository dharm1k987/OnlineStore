package com.android.group0674.onlinestore.Model.database;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableObject implements Serializable {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = -6449139165464216072L;
  private ArrayList<Object> itemsSerialized = null;

  public SerializableObject(ArrayList<Object> itemsToSerialize) {
    this.itemsSerialized = new ArrayList<>();
    this.itemsSerialized = itemsToSerialize;
  }

  public void addItemToSerialize(Object object) {
    this.itemsSerialized.add(object);
  }

  public ArrayList<Object> getItemsSerialized() {
    return this.itemsSerialized;
  }

}
