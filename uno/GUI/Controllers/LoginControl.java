package uno.GUI.Controllers;

import uno.Game.GameClient;
import uno.GUI.LoginControl.LoginData;
import uno.GUI.Panels.*;
import uno.Game.Player;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginControl implements ActionListener
{
  // Private data fields for the container and chat client.
  private JPanel container;
  private GameClient client;
  
  // Constructor for the login controller.
  public LoginControl(JPanel container, GameClient client)
  {
    this.container = container;
    this.client = client;
  }
  
  // Handle button clicks.
  public void actionPerformed(ActionEvent ae)
  {
    // Get the name of the button clicked.
    String command = ae.getActionCommand();

    // The Cancel button takes the user back to the initial panel.
    if (command == "Cancel")
    {
      CardLayout cardLayout = (CardLayout)container.getLayout();
      cardLayout.show(container, "Initial");
    }

    // The Submit button submits the login information to the server.
    else if (command == "Submit")
    {
      // Get the username and password the user entered.
      LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
      LoginData data = new LoginData(loginPanel.getUsername(), loginPanel.getPassword());
      
      // Check the validity of the information locally first.
      if (data.getUsername().equals("") || data.getPassword().equals(""))
      {
        displayError("You must enter a username and password.");
        return;
      }

      // Submit the login information to the server.
      try
      {
        client.sendToServer(data);
      }
      catch (IOException e)
      {
        displayError("Error connecting to the server.");
      }
    }
  }

  // After the login is successful, set the User object and display the contacts screen.
  public void loginSuccess()
  {
    LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
    Player player = new Player();

    player.setName(loginPanel.getUsername());
    client.setPlayer(player);

    CardLayout cardLayout = (CardLayout)container.getLayout();
    cardLayout.show(container, "Waiting");
  }

  // Method that displays a message in the error label.
  public void displayError(String error)
  {
    LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
    loginPanel.setError(error);
  }
}
