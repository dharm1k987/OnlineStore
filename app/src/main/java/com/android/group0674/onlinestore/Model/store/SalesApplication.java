package com.android.group0674.onlinestore.Model.store;

/*
import com.android.group0674.onlinestore.Model.database.DeserializeDB;
import com.android.group0674.onlinestore.Model.database.SerializeDB;*/
import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseInsertException;
import com.android.group0674.onlinestore.Model.exceptions.DatabaseMissingTableException;
import com.android.group0674.onlinestore.Model.exceptions.InvalidArgumentException;
import com.android.group0674.onlinestore.Model.inventory.Inventory;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.Model.users.Employee;
import com.android.group0674.onlinestore.Model.users.EmployeeInterface;
import com.android.group0674.onlinestore.Model.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;

// This is a comment to test svn

public class SalesApplication {
  /**
   * This is the main method that will run the entire application. instructions to finish this off.
   * 
   * @param argv - decide what option we choose.
   */
  public static void main(String[] argv) {/*
    //DeserializeDB.deserialize();
    // used for input11
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    if (connection == null) {
      System.out.print("NOOO");
    }
    try {

      if (argv.length == 0) {
        // no command line arguments
        throw new InvalidArgumentException("You must enter one command line argument.");
      }
      if (argv[0].equals("-1")) {

        DatabaseDriverExtender.initialize(connection);
        // insert the initial roles in the database
        int roleId1 = DatabaseInsertHelper.insertRole("EMPLOYEE");
        int roleId2 = DatabaseInsertHelper.insertRole("CUSTOMER");

        if (roleId1 == -1 || roleId2 == -1) {
          // stop the program since we need these roles inserted for sure
          throw new InvalidArgumentException("Error: One or more of initial roles were not in"
              + " enum, or they already exist in DB.");
        }

        // insert initial items into the database
        int itemId1 = DatabaseInsertHelper.insertItem("FISHING_ROD", new BigDecimal("50.00"));
        int itemId2 = DatabaseInsertHelper.insertItem("HOCKEY_STICK", new BigDecimal("100.00"));
        int itemId3 = DatabaseInsertHelper.insertItem("SKATES", new BigDecimal("75.00"));
        int itemId4 = DatabaseInsertHelper.insertItem("RUNNING_SHOES", new BigDecimal("60.00"));
        int itemId5 = DatabaseInsertHelper.insertItem("PROTEIN_BAR", new BigDecimal("10.00"));
        if (itemId1 == -1 || itemId2 == -1 || itemId3 == -1 || itemId4 == -1 || itemId5 == -1) {
          // stop the program since we need these items inserted for sure with valid prices
          throw new InvalidArgumentException("Error: One or more of initial items were not in"
              + " enum, or the price was not a > 0, 2 decimal number, or already in DB.");
        }

        // set the quantity of each of these items to 100 initially in the inventory
        int inventoryId1 = DatabaseInsertHelper.insertInventory(itemId1, 100);
        int inventoryId2 = DatabaseInsertHelper.insertInventory(itemId2, 100);
        int inventoryId3 = DatabaseInsertHelper.insertInventory(itemId3, 100);
        int inventoryId4 = DatabaseInsertHelper.insertInventory(itemId4, 100);
        int inventoryId5 = DatabaseInsertHelper.insertInventory(itemId5, 100);
        if (inventoryId1 == -1 || inventoryId2 == -1 || inventoryId3 == -1 || inventoryId4 == -1
            || inventoryId5 == -1) {
          // stop the program since we need these items inserted for sure with valid prices
          throw new InvalidArgumentException(
              "Error: One or more of the items could not be" + " inserted in the inventory.");
        }


        boolean employeeCreation = false;
        // create an employee, and loop until we get one
        System.out.println("EMPLOYEE:");
        while (!employeeCreation) {
          employeeCreation = createSpecificAccount(DatabaseSelectHelper.getRoleIdByName("EMPLOYEE"),
              bufferedReader);
          if (!employeeCreation) {
            System.out.println("One or more of the inputs were invalid. Try again.");
          }
        }

      } else if (argv[0].equals("0")) {
        bufferedReader.close();
        return;

      } else {
        // otherwise we want to show this screen
        System.out.println("Select one of the following options:");
        System.out.println("1 - Employee Login");
        System.out.println("2 - Customer Login");
        System.out.println("0 - Exit");
        String line;
        // get the inventory from the database
        Inventory inventory = DatabaseSelectHelper.getInventory();
        // loop until we get a 0 to return
        while (!(line = bufferedReader.readLine()).equals("0")) {
          // if its a 1
          if (line.equals("1")) {
            System.out.println("EMPLOYEE LOGIN: ");
            // EMPLOYEE SECTION
            System.out.print("Enter employee id: ");
            int employeeId = Integer.parseInt(bufferedReader.readLine());
            // check if id is valid id and it corresponds to employee
            User userEmployee = null;
            if (((userEmployee = DatabaseSelectHelper.getUserDetails(employeeId)) == null)
                || (!DatabaseSelectHelper.getRoleName(userEmployee.getRoleId())
                    .equals("EMPLOYEE"))) {
              System.out.println("Not a valid employee id.");
              // not valid, end session
              bufferedReader.close();
              return;
            } else {
              // otherwise it is so ask for password
              System.out.print("Enter password: ");
              // attempt to authenticate
              if (userEmployee.authenticate(bufferedReader.readLine())) {
                // successful, set authentication to true
                userEmployee.setAuthenticated(true);
              } else {
                // otherwise incorrect password, end the session
                bufferedReader.close();
                return;
              }
            }
            // we are authenticated now
            // create the employee interface object
            EmployeeInterface employeeInterface =
                new EmployeeInterface((Employee) userEmployee, DatabaseSelectHelper.getInventory());

            System.out.println("Select one of the following options:");
            System.out.println("1 - Authenticate new employee");
            System.out.println("2 - Make new Customer");
            System.out.println("3 - Make new Account");
            System.out.println("4 - Make new Employee");
            System.out.println("5 - Restock Inventory");
            System.out.println("6 - Exit");
            System.out.println("7 - View Books");
            System.out.println("8 - Get List of Accounts");

            // loop until they don't exit using 6
            String employeeOptions;
            while (!(employeeOptions = bufferedReader.readLine()).equals("6")) {
              if (employeeOptions.equals("1")) {
                // authenticate new employee
                System.out.print("Enter the id of the employee you wish to authenticate: ");
                // check if valid employee
                User userEmployeeAuth;
                if ((userEmployeeAuth = DatabaseSelectHelper
                    .getUserDetails(Integer.parseInt(bufferedReader.readLine()))) == null
                    || (!DatabaseSelectHelper.getRoleName(userEmployeeAuth.getRoleId())
                        .equals("EMPLOYEE"))) {
                  // this means that the userId was invalid, or they are not an employee
                  System.out.println("Invalid user id.");
                } else {
                  // otherwise it exists, and so we should authenticate
                  boolean pwdCorrect = false;
                  while (!pwdCorrect) {
                    System.out.print("Enter the password: ");
                    pwdCorrect = userEmployeeAuth.authenticate(bufferedReader.readLine());
                  }
                }
              } else if (employeeOptions.equals("2")) {
                // create customer
                boolean customerCreation = false;
                // create an employee, and loop until we get one
                System.out.println("CUSTOMER:");
                while (!customerCreation) {
                  customerCreation = createSpecificAccount(
                      DatabaseSelectHelper.getRoleIdByName("CUSTOMER"), bufferedReader);
                  if (!customerCreation) {
                    System.out.println("One or more of the inputs were invalid. Try again.");
                  }
                  System.out.println("---------\n");
                }
              } else if (employeeOptions.equals("3")) {
                User customerAccount;
                System.out
                    .println("Enter the id of the customer you want to create an account for: ");

                // make sure they are a customer
                if (((customerAccount = DatabaseSelectHelper
                    .getUserDetails(Integer.parseInt(bufferedReader.readLine()))) == null)
                    || (!DatabaseSelectHelper.getRoleName(customerAccount.getRoleId())
                        .equals("CUSTOMER"))) {
                  // this means that the userId was invalid, or they are not an employee
                  System.out.println("Invalid customer id.");
                } else {
                  employeeInterface.createAccount((Customer) customerAccount);
                }

              } else if (employeeOptions.equals("4")) {
                // create employee
                boolean employeeCreation = false;
                // create an employee, and loop until we get one
                System.out.println("EMPLOYEE:");
                while (!employeeCreation) {
                  employeeCreation = createSpecificAccount(
                      DatabaseSelectHelper.getRoleIdByName("EMPLOYEE"), bufferedReader);
                  if (!employeeCreation) {
                    System.out.println("One or more of the inputs were invalid. Try again.");
                  }
                  System.out.println("---------\n");
                }
              } else if (employeeOptions.equals("5")) {

                System.out.println("\nRESTOCK INVENTORY");
                // print out contents of the inventory
                inventory.displayInventoryMapping();
                System.out.print("Enter the item id you wish to change (-1) to exit: ");
                String itemId;
                int newQuantity;
                while (!(itemId = bufferedReader.readLine()).equals("-1")) {
                  // get the new quantity
                  System.out.print("Enter the new quantity: ");
                  newQuantity = Integer.parseInt(bufferedReader.readLine());
                  // attempt to change it
                  boolean successful = employeeInterface.restockInventory(
                      DatabaseSelectHelper.getItem(Integer.parseInt(itemId)), newQuantity);
                  if (!successful) {
                    System.out.print("Either the quantity or itemId is invalid. Try again.\n\n");
                  }
                  // get the inventory from the server since it may have changed
                  System.out.println("\n");
                  inventory = DatabaseSelectHelper.getInventory();
                  // print the contents
                  inventory.displayInventoryMapping();
                  System.out.print("Enter the item id you wish to change (-1 to go back): ");
                }
              } else if (employeeOptions.equals("7")) {
                // view the books
                ((Employee) userEmployee).viewBooks();


              } else if (employeeOptions.equals("8")) {
                // prompt the user for the cusotmer id
                System.out.println("Enter a customer id whose accounts you would like to view : ");
                int custId = Integer.parseInt(bufferedReader.readLine());

                // if the customerId is not valid
                User userCustomer = null;
                if (((userCustomer = DatabaseSelectHelper.getUserDetails(custId)) == null)
                    || (!DatabaseSelectHelper.getRoleName(userCustomer.getRoleId())
                        .equals("CUSTOMER"))) {
                  System.out.println("Not a valid customer id.");
                } else {
                  System.out.println("1 - Active accounts");
                  System.out.println("2 - Inactive accounts");
                  int choice = 0;
                  while (choice != 1 && choice != 2) {
                    System.out.println("Choose a valid option: ");
                    choice = Integer.parseInt(bufferedReader.readLine());
                    // keep looping until valid choice
                    if ((choice) == 1) {
                      ((Employee) userEmployee).displayAccounts(custId, 1);
                    } else if ((choice) == 2) {
                      ((Employee) userEmployee).displayAccounts(custId, 2);
                    }
                  }

                }

              }
              System.out.println("Select one of the following options:");
              System.out.println("1 - Authenticate new employee");
              System.out.println("2 - Make new Customer");
              System.out.println("3 - Make new Account");
              System.out.println("4 - Make new Employee");
              System.out.println("5 - Restock Inventory");
              System.out.println("6 - Exit");
              System.out.println("7 - View Books");
              System.out.println("8 - Get List of Accounts");;
            }
            // now its 6, so exit
            bufferedReader.close();
            return;

          } else if (line.equals("2")) {
            System.out.println("\nCUSTOMER LOGIN:");

            boolean validInfo = false;
            User userCustomer = null;
            while (!validInfo) {
              // get the customer id
              System.out.print("Enter the id of the customer: ");
              int customerId = Integer.parseInt(bufferedReader.readLine());
              // get the user
              userCustomer = DatabaseSelectHelper.getUserDetails(customerId);
              // if its null or not a customer, tell them
              if ((userCustomer == null) || (!DatabaseSelectHelper
                  .getRoleName(userCustomer.getRoleId()).equals("CUSTOMER"))) {
                System.out.println("This id does not belong to a customer.");
              } else {
                // they are a customer, so authenticate them
                boolean pwdCorrect = false;
                while (!pwdCorrect) {
                  System.out.print("Enter the password: ");
                  pwdCorrect = userCustomer.authenticate(bufferedReader.readLine());
                }
                // now they are verified
                validInfo = true;
                userCustomer.setAuthenticated(true);
              }
            }

            // tasks the customer can do
            System.out.println("Select one of the following options:");
            System.out.println("1 - List current items in cart");
            System.out.println("2 - Add a quantity of an item to the cart");
            System.out.println("3 - Check total price of items in cart");
            System.out.println("4 - Remove a quantity of an item in the cart");
            System.out.println("5 - Checkout");
            System.out.println("6 - Exit");
            System.out.println("7 - Restore shopping cart");
            System.out.println("8 - Request account creation");


            // create their shopping cart
            ShoppingCart shoppingCart = new ShoppingCart((Customer) userCustomer);
            inventory = DatabaseSelectHelper.getInventory();
            String customerOptions;
            // loop until they give the exit code
            while (!(customerOptions = bufferedReader.readLine()).equals("6")) {
              if (customerOptions.equals("1")) {
                // display list of items in cart
                shoppingCart.displayItemsInCart();
              } else if (customerOptions.equals("2")) {
                // add quantity of one item to cart
                inventory.displayInventoryMapping();
                System.out.print("Enter the item id you wish to add (-1) to exit: ");
                String itemId;
                int quantityToAdd;
                while (!(itemId = bufferedReader.readLine()).equals("-1")) {
                  // get the new quantity
                  System.out.print("Enter the new quantity: ");
                  quantityToAdd = Integer.parseInt(bufferedReader.readLine());
                  // attempt to change it
                  shoppingCart.addItem(DatabaseSelectHelper.getItem(Integer.parseInt(itemId)),
                      quantityToAdd);
                  shoppingCart.displayItemsInCart();
                  // loop again
                  System.out.print("Enter the item id you wish to change (-1) to exit: ");
                }
              } else if (customerOptions.equals("3")) {
                // display their total price
                System.out.println("TOTAL PRICE (without tax): " + shoppingCart.getTotal());
              } else if (customerOptions.equals("4")) {
                // remove quantity of one item to cart
                shoppingCart.displayItemsInCart();
                System.out.print("Enter the item id you wish to remove (-1) to exit: ");
                String itemId;
                int quantityToRemove;
                // loop until they are done removing
                while (!(itemId = bufferedReader.readLine()).equals("-1")) {
                  // get the new quantity
                  System.out.print("Enter the quantity you wish to remove: ");
                  quantityToRemove = Integer.parseInt(bufferedReader.readLine());
                  // attempt to change it
                  shoppingCart.removeItem(DatabaseSelectHelper.getItem(Integer.parseInt(itemId)),
                      quantityToRemove);
                  shoppingCart.displayItemsInCart();
                  System.out.print("Enter the item id you wish to remove (-1) to exit: ");
                }
              } else if (customerOptions.equals("5")) {
                // CHECKOUT
                boolean successful = shoppingCart.checkOut(shoppingCart);
                if (successful) {
                  System.out.println("Your total with tax is " + shoppingCart.getTotal());
                  System.out.println("Successfully checked out, you may shop again if you like.\n");
                  // create a new shopping cart and get an updated inventory
                  shoppingCart = new ShoppingCart((Customer) userCustomer);
                  inventory = DatabaseSelectHelper.getInventory();
                } else {
                  System.out.println("Failed to checkout, not enough inventory.");
                }
              } else if (customerOptions.equals("7")) {
                // Restore shopping cart, if there is an account
                try {
                  shoppingCart = DatabaseSelectHelper.restoreShoppingCart(
                      ((Customer) userCustomer).getAccount().getAccountId() - 1,
                      (Customer) userCustomer);
                } catch (NullPointerException e) {
                  bufferedReader.close();
                  throw new NullPointerException("There is no account associated with this id.");
                }
              } else if (customerOptions.equals("8")) {

                System.out.print("Enter employee id: ");
                int employeeId = Integer.parseInt(bufferedReader.readLine());
                // check if id is valid id and it corresponds to employee
                User userEmployee = null;
                if (((userEmployee = DatabaseSelectHelper.getUserDetails(employeeId)) == null)
                    || (!DatabaseSelectHelper.getRoleName(userEmployee.getRoleId())
                        .equals("EMPLOYEE"))) {
                  System.out.println("Not a valid employee id.");
                  // not valid, end session
                  bufferedReader.close();
                  return;
                } else {
                  // otherwise it is so ask for password
                  System.out.print("Enter password: ");
                  // attempt to authenticate
                  if (userEmployee.authenticate(bufferedReader.readLine())) {
                    // successful, set authentication to true
                    userEmployee.setAuthenticated(true);
                    // create employeeInterface and the account
                    EmployeeInterface employeeInterface = new EmployeeInterface(
                        ((Employee) userEmployee), DatabaseSelectHelper.getInventory());
                    employeeInterface.createAccount((Customer) userCustomer);
                  } else {
                    // otherwise incorrect password, end the session
                    bufferedReader.close();
                    return;
                  }
                }
              }

              // tasks the customer can do
              System.out.println("Select one of the following options:");
              System.out.println("1 - List current items in cart");
              System.out.println("2 - Add a quantity of an item to the cart");
              System.out.println("3 - Check total price of items in cart");
              System.out.println("4 - Remove a quantity of an item in the cart");
              System.out.println("5 - Checkout");
              System.out.println("6 - Exit");
              System.out.println("7 - Restore shopping cart");
              System.out.println("8 - Request account creation");
            }
            // now its 6 so we must exit
            bufferedReader.close();
            return;
            // from our 3 options, if it was 0, exit
          }
          System.out.print("Enter a new option: ");
        }
      }

    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      System.out.println(e.getMessage());
    } catch (ClassCastException e) {
      // this happens when we convert an employee to admin, and then try to convert them
      // again in the same session
      System.out.println("This user is already an admin, reload the program");
    } catch (NumberFormatException e) {
      // this happens when the user enters a string/null as the id
      System.out.println("The integer value was not valid.");
    } catch (Exception e) {
      System.out.println(e.getClass().getName());
    } finally {
      try {
        System.out.println("<--------- FINISHED --------->");
        connection.close();
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }*/
  }

  /**
   * This method will simply create a user and return whether it was done successfully. Since we
   * create many users through our prompts, they all use this method.
   * 
   * @param roleIdByName - the id of the role we want to become (admin, employee, customer).
   * @param bufferedReader - the input stream we will be reading from.
   * @return - true if successful, false otherwise (invalid inputs from the keyboard).
   * @throws IOException - if an error occurs.
   */
  private static boolean createSpecificAccount(Integer roleIdByName, BufferedReader bufferedReader)
      throws IOException {/*
    // ask for details
    System.out.print("Enter the name (first and last): ");
    String name = bufferedReader.readLine();
    int age;
    try {
      // if their age is not valid, return false (a string given)
      System.out.print("Enter the age (> 0): ");
      age = Integer.parseInt(bufferedReader.readLine());
    } catch (NumberFormatException e) {
      return false;
    }
    // get address and password
    System.out.print("Enter the address age (< 100 chars): ");
    String address = bufferedReader.readLine();
    System.out.print("Enter the password: ");
    String password = bufferedReader.readLine();

    int userId;
    int roleId;
    try {
      // try to insert, if it fails we know something was wrong
      if ((userId = DatabaseInsertHelper.insertNewUser(name, age, address, password)) == -1) {
        return false;
      } else {
        // otherwise we want to insert the role now too
        if ((roleId = DatabaseInsertHelper.insertUserRole(userId, roleIdByName)) == -1) {
          return false;
        }
        // otherwise everything is valid, so tell the user their id
        System.out.println("Your id is " + userId + "\n");
        return true;
      }
    } catch (DatabaseInsertException | DatabaseMissingTableException | NumberFormatException e) {
      return false;
    }*/
    return false;
  }


}
