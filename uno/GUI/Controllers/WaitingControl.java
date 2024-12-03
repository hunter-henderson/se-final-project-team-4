package uno.GUI.Controllers;

import uno.GUI.Panels.GamePanel;
import uno.GUI.Panels.WaitingPanel;
import uno.Game.GameClient;
import uno.Game.Player;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class WaitingControl implements ActionListener
{
    // Private data field for storing the container.
    private JPanel container;
    private GameClient client;
    private GameControl gc;

    // Constructor for the initial controller.
    public WaitingControl(JPanel container, GameClient client, GameControl gc)
    {
        this.container = container;
        this.client = client;
        this.gc = gc;
    }

    // Handle button clicks.
    public void actionPerformed(ActionEvent ae)
    {
        // Get the name of the button clicked.
        String command = ae.getActionCommand();

        // The Return button takes the user to the login panel.
        if (command.equals("Return to Main Menu."))
        {
            try
            {
                client.sendToServer("PlayerDrop");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

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

    public GameClient getClient()
    {
        return client;
    }

    // After the host starts game, well, start the game.
    public void gameStarted()
    {
        gc.initializeTurn();
        GamePanel gamePanel = new GamePanel(this.gc, client.getPlayer());
        gamePanel.setGame(gamePanel.getGame());

        container.add(gamePanel, "Game");
        container.repaint();
        container.revalidate();

        CardLayout cardLayout = (CardLayout)container.getLayout();
        cardLayout.show(container, "Game");
    }

}