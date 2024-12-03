package uno;

import java.awt.*;
import javax.swing.*;

import uno.Game.GameServer;
import uno.GUI.LoginControl.DatabaseFile;

import java.awt.event.*;
import java.io.IOException;

public class ServerGUI extends JFrame
{
  //Data fields.
  private JLabel statusLabel;
  private JLabel startGameLabel;
  private JLabel status;
  private JLabel playerCount;
  private JTextArea log;
  private JButton startGame;
  private JButton stopHosting;
  private GameServer server;

  // Constructor for the server GUI.
	public ServerGUI()
    {
        // Set the title and default close operation.
        this.setTitle("UNO! Server");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the main variables that will be used.
        JPanel north = new JPanel();
        JPanel center = new JPanel(new BorderLayout());
        JPanel south = new JPanel();
        EventHandler handler = new EventHandler();
        int i = 0;

        // Create the server status label.
        JLabel statusText = new JLabel("Status:");
        north.add(statusText);
        status = new JLabel("Not Connected");
        status.setForeground(Color.RED);
        north.add(status);

        // Create the server log panel.
        JPanel serverLogPanel = new JPanel(new BorderLayout());
        JLabel serverLabel = new JLabel("Server Log", JLabel.CENTER);
        JPanel serverLabelBuffer = new JPanel();
        serverLabelBuffer.add(serverLabel);
        serverLogPanel.add(serverLabelBuffer, BorderLayout.NORTH);
        log = new JTextArea(10, 35);
        log.setEditable(false);
        JScrollPane serverLogPane = new JScrollPane(log);
        JPanel serverLogPaneBuffer = new JPanel();
        serverLogPaneBuffer.add(serverLogPane);
        serverLogPanel.add(serverLogPaneBuffer, BorderLayout.SOUTH);

        // Add the server log panel to the south part of the center.
        JPanel centerSouth = new JPanel();
        centerSouth.add(serverLogPanel);
        center.add(centerSouth, BorderLayout.SOUTH);

        // Create the database object.
        DatabaseFile database = new DatabaseFile();

        // Set up the chat server object.
        server = new GameServer();
        server.setLog(log);
        server.setStatus(status);
        server.setDatabase(database);

        server.setPort(8300);
        server.setTimeout(500);

        try
        {
            server.listen();
        }
        catch (IOException e1) {
            log.append("An exception occurred: " + e1.getMessage() + "\n");
        }

        //Display player counts.
        JPanel centerNorth = new JPanel();
        centerNorth.setLayout(new BoxLayout(centerNorth, BoxLayout.Y_AXIS));

        JPanel playerCountBuffer = new JPanel();
        JLabel playerCountLabel = new JLabel("Current Playercount: ");
        playerCountBuffer.add(playerCountLabel);

        playerCount = new JLabel(this.server.getGame().getPlayerCount() + "/8");
        playerCountBuffer.add(playerCount);

        server.setPlayerCount(playerCount);

        JPanel errorBuffer = new JPanel();
        startGameLabel = new JLabel("Failed to start game: ");
        startGameLabel.setForeground(Color.RED);
        startGameLabel.setVisible(false);
        errorBuffer.add(startGameLabel);

        statusLabel = new JLabel("");
        statusLabel.setVisible(false);
        errorBuffer.add(statusLabel);

        centerNorth.add(playerCountBuffer);
        centerNorth.add(errorBuffer);

        JPanel centerNorthBuffer = new JPanel();
        centerNorthBuffer.add(centerNorth);
        center.add(centerNorthBuffer, BorderLayout.NORTH);

        // Create the buttons.
        startGame = new JButton("Start Game");
        startGame.addActionListener(handler);
        south.add(startGame);

        stopHosting = new JButton("Stop Hosting");
        stopHosting.addActionListener(handler);
        south.add(stopHosting);

        server.setStopHosting(stopHosting);

        // Add the north, center, and south JPanels to the JFrame.
        this.add(north, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.add(south, BorderLayout.SOUTH);

        // Display the window.
        this.setSize(450, 450);
        this.setVisible(true);
    }

  // Main function that creates a server GUI when the program is started.
	public static void main(String[] args)
	{
		new ServerGUI();
	}

	public JLabel getStatus()
  {
	  return status;
  }
	public JTextArea getLog()
  {
	  return log;
  }
	
	// Class for handling events.
	class EventHandler implements ActionListener
	{
	  // Event handler for ActionEvent.
      public void actionPerformed(ActionEvent e)
      {
        // Determine which button was clicked.
        Object buttonClicked = e.getSource();
      
        // Handle the Listen button.
        if (buttonClicked == startGame)
        {
            if (server.getGame().getPlayerCount() > 1)
            {
                server.gameStart();

                startGameLabel.setText("Game started!");
                startGameLabel.setForeground(Color.GREEN);
                statusLabel.setText("Have fun!");

                startGameLabel.setVisible(true);
                statusLabel.setVisible(true);
            }

            else if (server.getGame().getPlayerCount() <= 1)
            {
                startGameLabel.setVisible(true);
                statusLabel.setVisible(true);

                statusLabel.setText("Not enough players. Must have at least 2.");
            }

            else if (server.getGame().getPlayerCount() > 8)
            {
                startGameLabel.setVisible(true);
                statusLabel.setVisible(true);

                statusLabel.setText("Too many players. Cannot have more than 8.");
            }
        }

      
        // For the Quit button, just stop this program.
        else if (buttonClicked == stopHosting)
        {
            server.stopServer();

            dispose();
        }
      }
	}
}
