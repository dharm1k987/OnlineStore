package com.android.group0674.onlinestore.Model.store;

import java.util.List;

/**
 * This is the interface for the SalesLog.
 * 
 * @author dharmik
 *
 */
public interface SalesLog {

  public void addSale(Sale sale);

  public List<Sale> getAllSales();
}
