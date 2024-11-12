package lab7out;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

public class DatabaseFile
{
    private Connection conn;
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();
    FileInputStream fis;
    
    public DatabaseFile()
    {
    	try
    	{
    		// Open a FileInputStream
            fis = new FileInputStream("lab7out/db.properties");
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	Properties props = new Properties();
    	try {
    		props.load(fis);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	String url = props.getProperty("url");
        String user = props.getProperty("user");
        String pass = props.getProperty("password");
        try {
        	// Set the connection
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // Sets up variables of the usernames and passwords for verification purposes
        String usernameQuery = "SELECT username FROM users";
        usernames = this.query(usernameQuery);
        
        String passwordQuery = "SELECT password FROM users";
        passwords = this.query(passwordQuery);
    }
    
    public ArrayList<String> query(String query)
    {
        // Variable Declaration/Initialization
        ArrayList<String>list = new ArrayList<String>();
        int count = 0;
        int noColumns = 0;
        
        try
        {
            // Create the database statement
            Statement statement = conn.createStatement();
            
            // Get the ResultSet
            ResultSet rs = statement.executeQuery(query);
            
            // Get the ResultSetMetaData (number of columns)
            ResultSetMetaData rmd = rs.getMetaData();
            
            // Get the number of columns
            noColumns = rmd.getColumnCount();
            
            while(rs.next())
            {
                String record = "";
                
                // Iterate through each field
                for (int i = 0; i < noColumns; i++)
                {
                    record += rs.getString(i+1);
                    record += ",";
                }
                list.add(record);
            }
            
            // Check for empty list
            if (list.size() == 0)
            {
                return null;
            }
            
            return list;
            
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public void executeDML(String dml) throws SQLException
    {
      //Add your code here
        Statement statement = conn.createStatement();
        
        statement.execute(dml);
    }
    
 // Method for verifying a username and password.
    public boolean verifyAccount(String username, String password)
    {
    	// Above in the query function, we add a comma to the end of the query results
    	String usernameCheck = username + ",";
    	String passwordCheck = password + ",";
    	
    	if (usernames.contains(usernameCheck) && passwords.contains(passwordCheck))
    		return true;
      
      return false;
    }
    
    // Method for creating a new account.
    public boolean createNewAccount(String username, String password)
    {
    	boolean usernameExists = false;
    	String usernameCheck = username + ",";
    	
    	if (usernames.contains(usernameCheck))
    		usernameExists = true;
      
      // Stop if this account already exists.
      if (usernameExists == true)
        return false;
      
      String addAccountQuery = "INSERT INTO users (username, password)\n"
      		+ "VALUES\n"
      		+ "	('" + username + "', '" + password + "');";
      
      // Add the new account.
      try {
		this.executeDML(addAccountQuery);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      return true;
    }
}
