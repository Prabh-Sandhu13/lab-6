package mysql_access;

import java.sql.*;
import java.util.Properties;
	 
//For db.cs.dal.ca, you must be running under Dal's VPN software

public class DBAccess {

	    // Open a connection to a database, submit a query for the contents of a table, and
	    // print the output.
	    public static void main(String[] args) {
	    	// Variables for the connections and the queries.
	        Connection connect = null;      // the link to the database
	        Statement statement = null;     // a place to build up an SQL query
	        ResultSet resultSet = null;     // a data structure to receive results from an SQL query
	        
	        // Info used for the connection that you will definitely want to change, make
	        // into parameters, or draw from environment variables instead of having hard-coded

	        Properties identity = new Properties();  //  Using a properties structure, just to hide info from other users.
	        MyIdentity me = new MyIdentity();        //  My own class to identify my credentials.  Ideally load Properties from a file instead and this class disappears.

	        String user;
	        String password;
	        String dbName;
	        String query = "select * from employees limit 3;";

	        // Get the info for logging into the database.

	        me.setIdentity( identity );                   // Fill the properties structure with my info.  Ideally, load properties from a file instead to replace this bit.
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
	            resultSet = statement.executeQuery( query );

	            // Print the output of a resultSet that extracted the employee table.

	            // ResultSet is the sequence of returned rows.  Its initial position is
	            // before the first data set so calling "next"
	            // at the start queues up the first answer.
	            while (resultSet.next()) {
	                // It is possible to get the columns via name.
	                // It is also possible to get the columns via the column number,
	                // which starts at 1.
	                // e.g. resultSet.getSTring(2);
	                System.out.println("Employee number: " + resultSet.getInt(1));
	                System.out.println("Last Name: " + resultSet.getString("lastName"));
	                System.out.println("First Name: " + resultSet.getString("firstName"));
	            }

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
