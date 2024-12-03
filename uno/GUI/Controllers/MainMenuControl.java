package uno.GUI.Controllers;

import uno.Game.GameClient;
import uno.GUI.Panels.*;
import uno.Game.GameServer;
import uno.ServerGUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class MainMenuControl implements ActionListener
{
    // Private data field for storing the container.
    private JPanel container;
    private GameClient client;
    private ServerGUI server;

    // Constructor for the initial controller.
    public MainMenuControl(JPanel container, GameClient client)
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
        if (command.equals("Host Game"))
        {
            WaitingPanel waitingPanel = (WaitingPanel) container.getComponent(4);
            waitingPanel.isHosting(true);

            server = new ServerGUI();

            try
            {
                client.openConnection();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            CardLayout cardLayout = (CardLayout)container.getLayout();
            cardLayout.show(container, "Initial");
        }

        // The Create button takes the user to the create account panel.
        else if (command.equals("Join Game"))
        {
            WaitingPanel waitingPanel = (WaitingPanel) container.getComponent(4);
            waitingPanel.isHosting(false);

            try
            {
                client.openConnection();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            CardLayout cardLayout = (CardLayout)container.getLayout();
            cardLayout.show(container, "Initial");
        }

        // The Logout button takes the user to initial panel.
        else if (command.equals("Log Out"))
        {
            CardLayout cardLayout = (CardLayout)container.getLayout();
            cardLayout.show(container, "Initial");
        }
    }
}
