package uno.GUI.LoginControl;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


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
			  //Create properties object 
			  Properties props = new Properties();
			  
			  //open file input stream
			  FileInputStream fis = new FileInputStream("src/uno/GUI/LoginControl/db.properties");
			  
			  props.load(fis);
			  
			  String url = props.getProperty("url");
			  String user = props.getProperty("user");
			  String pass = props.getProperty("password");
			  
			  //set connection
			 conn = DriverManager.getConnection(url, user, pass);
			  
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
	    
	  }

    public Connection getConnection()
    {
        return conn;
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
        Statement statement = conn.createStatement();
        statement.execute(dml);
    }
    
    // Method for verifying a username and password.
    public boolean verifyAccount(String username, String password) {
		// Read the database.
		int i = 0;
		
		ArrayList<String> accountList = new ArrayList<String>(); 
        accountList = query("select * from user");

        if (accountList == null)
        {
            return false;
        }
	    

        for (String s : accountList) {
			
            //set array to current line
            String[] arrOfStr = s.split(",");
            i = 0;

            boolean check1 = false;
            for (String s2 : arrOfStr) {
                if(i % 2 == 0) {
                    //check if username matches
                    if(s2.equals(username)) {
                        //passed check one
                        check1 = true;
                        i = i+1;
						 
                    }
                    else
                    {
                        check1 = false;
                        i = i+1;
						 
                    }
					 
                }
                else if (i % 2 != 0)
                {
					 
					 if(check1 == true) {
						 //if username exists check password for username matches 
						 	if(s2.equals(password)) 
						 	{
						 		return true;
						 	}
					 
					 }
					 
					 
                }
					
            }
			 
		
        }
	
        //No matches found in for loop
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
      
      String addAccountQuery = "INSERT INTO user (username, password)\n"
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
