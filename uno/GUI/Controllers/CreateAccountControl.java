package uno.GUI.Controllers;

import uno.Game.GameClient;
import uno.Game.Player;
import uno.GameGUI;
import uno.GUI.LoginControl.*;
import uno.GUI.Panels.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreateAccountControl implements ActionListener
{
  // Private data fields for the container and chat client.
  private JPanel container;
  private GameClient client;
  
  // Constructor for the create account controller.
  public CreateAccountControl(JPanel container, GameClient client)
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

    // The Submit button creates a new account.
    else if (command == "Submit")
    {
      // Get the text the user entered in the three fields.
      CreateAccountPanel createAccountPanel = (CreateAccountPanel)container.getComponent(2);
      String username = createAccountPanel.getUsername();
      String password = createAccountPanel.getPassword();
      String passwordVerify = createAccountPanel.getPasswordVerify();

      // Check the validity of the information locally first.
      if (username.equals("") || password.equals(""))
      {
        displayError("You must enter a username and password.");
        return;
      }
      else if (!password.equals(passwordVerify))
      {
        displayError("The two passwords did not match.");
        return;
      }
      if (password.length() < 6)
      {
        displayError("The password must be at least 6 characters.");
        return;
      }
      
      // Submit the new account information to the server.
      CreateAccountData data = new CreateAccountData(username, password);
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

  // After an account is created, set the User object and display the contacts screen.
  public void createAccountSuccess()
  {
    CreateAccountPanel createAccountPanel = (CreateAccountPanel)container.getComponent(2);
    GameGUI clientGUI = (GameGUI)SwingUtilities.getWindowAncestor(createAccountPanel);
    //clientGUI.setUser(new User(createAccountPanel.getUsername(), createAccountPanel.getPassword()));

    Player player = new Player();
    player.setName(createAccountPanel.getUsername());
    client.setPlayer(player);

    CardLayout cardLayout = (CardLayout)container.getLayout();
    cardLayout.show(container, "Waiting");
  }
  
  // Method that displays a message in the error label.
  public void displayError(String error)
  {
    CreateAccountPanel createAccountPanel = (CreateAccountPanel)container.getComponent(2);
    createAccountPanel.setError(error);
  }
}
