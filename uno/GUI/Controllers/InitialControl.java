package uno.GUI.Controllers;

import uno.Game.GameClient;
import uno.GUI.Panels.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class InitialControl implements ActionListener
{
  // Private data field for storing the container.
  private JPanel container;
  private GameClient client;
  // Constructor for the initial controller.
  public InitialControl(JPanel container, GameClient client)
  {
    this.container = container;
    this.client = client;
  }
  
  // Handle button clicks.
  public void actionPerformed(ActionEvent ae)
  {
    // Get the name of the button clicked.
    String command = ae.getActionCommand();
    
    // The Login button takes the user to the login panel.
    if (command.equals("Login"))
    {
      LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
      loginPanel.setError("");
      CardLayout cardLayout = (CardLayout)container.getLayout();
      cardLayout.show(container, "Login");
     
    }
    
    // The Create button takes the user to the create account panel.
    else if (command.equals("Create"))
    {
      CreateAccountPanel createAccountPanel = (CreateAccountPanel)container.getComponent(2);
      createAccountPanel.setError("");
      CardLayout cardLayout = (CardLayout)container.getLayout();
      cardLayout.show(container, "CreateAccount");
    }

    // The return to main menu button returns the user to main menu.
    else if (command.equals("Return to Main Menu."))
    {
      WaitingPanel waitingPanel = (WaitingPanel)container.getComponent(4);
      if (waitingPanel.hosting());
      {
        try
        {
          client.sendToServer("StopHosting");
        }
        catch (IOException e)
        {
          throw new RuntimeException(e);
        }
      }

      CardLayout cardLayout = (CardLayout)container.getLayout();
      cardLayout.show(container, "MainMenu");
    }
  }
}
