package uno;

import uno.Game.GameClient;
import uno.GUI.Controllers.*;
import uno.GUI.Panels.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameGUI extends JFrame
{
  // Constructor that creates the client GUI.
  public GameGUI()
  {
	  String ip = "localhost";
	    int port = 8300;

	    // Receive the IP address and port from the config.txt file
	    // The first argument is the IP address and the second is the port, with the space being delimiters.
	    FileInputStream stream = null;
	    try {
	      stream = new FileInputStream("config.txt");
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }

	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream)))
	    {
	      String fileContents;

	      // Read each line from the file
	      while ((fileContents = reader.readLine()) != null) {
	        // Split the line by space
	        String[] parts = fileContents.split(" ");

	        // Ensure there are two parts (IP and port)
	        if (parts.length == 2) {
	          ip = parts[0];
	          port = Integer.parseInt(parts[1]);

	          // Process the IP and port
	          System.out.println("IP Address: " + ip);
	          System.out.println("Port: " + port);
	        } else {
	          System.out.println("Invalid line format: " + fileContents);
	        }
	      }
	    } catch (FileNotFoundException e) {
	      System.err.println("File not found: " + stream);
	    } catch (IOException e) {
	      System.err.println("Error reading file: " + e.getMessage());
	    }

	    // Set up the chat client.
	    GameClient client = new GameClient();
	    client.setHost(ip);
	    client.setPort(port);

    // Set the title and default close operation.
    this.setTitle("UNO!");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    // Create the card layout container.
    CardLayout cardLayout = new CardLayout();
    JPanel container = new JPanel(cardLayout);
    
    //Create the Controllers
    InitialControl ic = new InitialControl(container,client);
    LoginControl lc = new LoginControl(container,client);
    CreateAccountControl cac = new CreateAccountControl(container,client);
    MainMenuControl mmc = new MainMenuControl(container,client);
    GameControl gc = new GameControl(container,client);
    WaitingControl wc = new WaitingControl(container,client,gc);
    GameOverControl goc = new GameOverControl(container,client);



    
    //Set the client info
    client.setLoginControl(lc);
    client.setCreateAccountControl(cac);
    client.setWaitingControl(wc);
    client.setGameControl(gc);
   
    
    // Create the four views. (need the controller to register with the Panels
    JPanel view1 = new InitialPanel(ic);
    JPanel view2 = new LoginPanel(lc);
    JPanel view3 = new CreateAccountPanel(cac);
    JPanel view4 = new MainMenuPanel(mmc);
    JPanel view5 = new WaitingPanel(wc);
    JPanel view6 = new GameOverPanel(goc);


    
    // Add the views to the card layout container.
    container.add(view1, "Initial");          //INDEX 0
    container.add(view2, "Login");            //INDEX 1
    container.add(view3, "CreateAccount");    //INDEX 2
    container.add(view4, "MainMenu");         //INDEX 3
    container.add(view5, "Waiting");          //INDEX 4
    container.add(view6, "GameOver");         //INDEX 5


    // Show the initial view in the card layout.
    cardLayout.show(container, "MainMenu");
    
    // Add the card layout container to the JFrame.
    // GridBagLayout makes the container stay centered in the window.
    this.setLayout(new GridBagLayout());
    this.add(container);

    // Show the JFrame.
    this.setSize(1000, 900);
    this.setVisible(true);
    this.pack();
  }

  // Main function that creates the client GUI when the program is started.
  public static void main(String[] args)
  {
    new GameGUI();
  }
}
