package com.android.group0674.onlinestore.Model.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the class that implements the salesLog.
 * 
 * @author dharmik
 *
 */
public class SalesLogImpl implements SalesLog, Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3433377456173104050L;
  private List<Sale> listOfSales = new ArrayList<>();

  /**
   * This method will add a sale to the salesLog list.
   * 
   * @param sale - the sale to add.
   */
  @Override
  public void addSale(Sale sale) {
    listOfSales.add(sale);
  }

  /**
   * This method will return a list of all the sales in the salesLog.
   * 
   * @return - the list of sales.
   */
  @Override
  public List<Sale> getAllSales() {
    return listOfSales;
  }

}
