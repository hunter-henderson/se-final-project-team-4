package uno.GUI.Controllers;

import uno.Game.GameClient;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GameOverControl implements ActionListener
{
    // Private data field for storing the container.
    private JPanel container;
    private GameClient client;

    // Constructor for the initial controller.
    public GameOverControl(JPanel container, GameClient client)
    {
        this.container = container;
        this.client = client;
    }

    // Handle button clicks.
    public void actionPerformed(ActionEvent ae)
    {
        // Get the name of the button clicked.
        String command = ae.getActionCommand();

        // The Return button takes the user to the login panel.
        if (command.equals("Return to Main Menu."))
        {
            CardLayout cardLayout = (CardLayout)container.getLayout();
            cardLayout.show(container, "MainMenu");
        }
    }
}
