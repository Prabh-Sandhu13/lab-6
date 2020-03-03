import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBAccess {
	// Open a connection to a database, submit a query for the contents of a table, and
    // print the output.
    public static void main(String[] args) {
    	// Variables for the connections and the queries.
        Connection connect = null;      // the link to the database
        Statement statement = null;     // a place to build up an SQL query
        Statement statement2 = null;
        ResultSet resultSet = null;     // a data structure to receive results from an SQL query
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        
        // Info used for the connection that you will definitely want to change, make
        // into parameters, or draw from environment variables instead of having hard-coded

        Properties identity = new Properties();  //  Using a properties structure, just to hide info from other users.
        String user;
        String password;
        String dbName;
//        String query = "select * from employees limit 3;";
        int OrderID;

//        String orderId= userInput();
        InputStreamReader ins = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(ins);
        
        System.out.println("Please enter the order ID");
    	
        while(true) {
        	
        	try {
				String a = br.readLine();
				OrderID = Integer.parseInt(a);
				break;
			} catch (IOException e) {
			}
        	catch (NumberFormatException n) {
        		System.out.println("Please enter a numerical value");
        	}
        	
        }
        
        // Two queries added for the required result
        String q="Select OrderDate,orders.OrderID,customers.CustomerID, customers.ContactName"
        		+ ", customers.Address, employees.EmployeeID, FirstName, LastName from "
        		+ "orders, employees, customers where OrderID = "+ OrderID +" and "
        		+ "orders.customerID = customers.CustomerID and orders.EmployeeID = employees.EmployeeID;";
        
        String q2="Select products.ProductID, products.ProductName, orderdetails.UnitPrice,"
        		+ " Quantity, Discount  from products, orderdetails where orderdetails.OrderID = "+ OrderID +" and"
        		+ " orderdetails.ProductID = products.ProductID;"; 


        // Get the info for logging into the database.

        MyIdentity.setIdentity( identity );                   // Fill the properties structure with my info.  Ideally, load properties from a file instead to replace this bit.
        user = identity.getProperty("user");
        password = identity.getProperty("password");
        dbName = identity.getProperty("database");

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false", user, password);

            // Statements allow to issue SQL queries to the database.  Create an instance
            // that we will use to ultimately send queries to the database.
            statement = connect.createStatement();

            // Choose a database to use
            statement.executeQuery("use " + dbName + ";");

            // Result set gets the result of the SQL query
//            resultSet = statement.executeQuery( query );
            resultSet2 = statement.executeQuery( q );
            
            statement2 = connect.createStatement();
            statement2.executeQuery("use " + dbName + ";");
            resultSet3 = statement2.executeQuery( q2 );
            

            // Print the output of a resultSet that extracted the employee table.

            // ResultSet is the sequence of returned rows.  Its initial position is
            // before the first data set so calling "next"
            // at the start queues up the first answer.
//            while (resultSet.next()) {
                // It is possible to get the columns via name.
                // It is also possible to get the columns via the column number,
                // which starts at 1.
                // e.g. resultSet.getSTring(2);
//                System.out.println("Employee number: " + resultSet.getInt(1));
//                System.out.println("Last Name: " + resultSet.getString("lastName"));
//                System.out.println("First Name: " + resultSet.getString("firstName"));
//            }
            
            while (resultSet2.next()) {
            	System.out.println("Order Date: "+ resultSet2.getDate("OrderDate"));
            	System.out.println("Order Number: "+ resultSet2.getInt("OrderID"));
            	System.out.println("Customer Name: "+ resultSet2.getString("ContactName"));
            	System.out.println("Customer Address: "+ resultSet2.getString("Address"));
            	System.out.println("Sales Representative: "+ resultSet2.getString("FirstName")+" "
            			+resultSet2.getString("LastName"));
            }
            System.out.println();
            double totalCost = 0;
            System.out.println("Product Code"+"	"+"Product Name");
            while (resultSet3.next()) {
            	System.out.println(" "+resultSet3.getString("ProductID")+"      	"+
            resultSet3.getString("ProductName"));
            	totalCost += (resultSet3.getInt("Quantity") * resultSet3.getDouble("UnitPrice")) -
            			resultSet3.getDouble("Discount");
            }

            System.out.println();
            System.out.println("Total cost of the order: "+totalCost);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            // Always close connections, otherwise the MySQL database runs out of them.

            // Close any of the resultSet, statements, and connections that are open and holding resources.
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
