package uno.Game;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import uno.GUI.LoginControl.CreateAccountData;
import uno.GUI.LoginControl.DatabaseFile;
import uno.GUI.LoginControl.LoginData;

public class GameServer extends AbstractServer
{
  // Data fields for this chat server.
  private JTextArea log;
  private JLabel status;
  private JLabel playerCount;
  private boolean running = false;
  private DatabaseFile database;
  private Game game;
  private JButton stopHosting;

  // Constructor for initializing the server with default settings.
  public GameServer()
  {
    super(12345);
    this.setTimeout(500);
    this.game = new Game();
  }

  // Getter that returns whether the server is currently running.
  public boolean isRunning()
  {
    return running;
  }
  
  // Setters for the data fields corresponding to the GUI elements.
  public void setLog(JTextArea log)
  {
    this.log = log;
  }
  public void setStatus(JLabel status)
  {
    this.status = status;
  }
  public void setPlayerCount(JLabel playerCount) {this.playerCount = playerCount;}
  public void setStopHosting(JButton stopHosting) {this.stopHosting = stopHosting;}
  
  public void setDatabase(DatabaseFile database)
  {
	  this.database = database;
  }

  // When the server starts, update the GUI.
  public void serverStarted()
  {

    running = true;
    status.setText("Listening");
    status.setForeground(Color.GREEN);
    log.append("Server started\n");


  }
  
  // When the server stops listening, update the GUI.
   public void serverStopped()
   {
     status.setText("Stopped");
     status.setForeground(Color.RED);
     log.append("Server stopped accepting new clients - press Listen to start accepting new clients\n");
   }
 
  // When the server closes completely, update the GUI.
  public void serverClosed()
  {
    running = false;
    status.setText("Close");
    status.setForeground(Color.RED);
    log.append("Server and all current clients are closed - press Listen to restart\n");
  }

  public void stopServer() {
    try
    {
      stopListening();
      close();
      running = false;
      if (log != null) log.append("Server fully shut down.\n");
    }
    catch (IOException e)
    {
      if (log != null) log.append("Error during shutdown: " + e.getMessage() + "\n");
    }
  }

  // When a client connects or disconnects, display a message in the log.
  public void clientConnected(ConnectionToClient client)
  {
    log.append("Client " + client.getId() + " connected\n");
  }

  // When a message is received from a client, handle it.
  public void handleMessageFromClient(Object arg0, ConnectionToClient arg1)
  {
    // If we received a card, play that card.
    if (arg0 instanceof Card)
    {
      game.cardPlayed((Card)arg0);

      boolean gameOver = game.winConditionMet();
      if (gameOver)
      {
        sendToAllClients(game.getWinner());
        stopHosting.doClick();
      }
      else
      {
        game.advanceTurn();
        sendToAllClients(game);
      }
    }

    // If we received LoginData, verify the account information.
    if (arg0 instanceof LoginData)
    {
      loginRequest(arg0, arg1);
    }
    
    // If we received CreateAccountData, create a new account.
    else if (arg0 instanceof CreateAccountData)
    {
      createAccountRequest(arg0, arg1);
    }

    // If we receive a string, figure out what the command is.
    else if (arg0 instanceof String)
    {
      String message = (String)arg0;

      // If we received QuitGame, create a new account.
      if (arg0 == "PlayerDrop")
      {
        dropPlayer(arg0, arg1);
      }

      // If we received QuitGame, create a new account.
      else if (message.equals("StopHosting"))
      {
        stopHosting.doClick();
      }

      //Player has drawn a card.
      else if (message.equals("DrawCard"))
      {
        game.draw(game.getPlayers().get(game.getCurrentPlayerIndex()));
        game.advanceTurn();
        sendToAllClients(game);
      }
    }
  }



  // Method that handles listening exceptions by displaying exception information.
  public void listeningException(Throwable exception) 
  {
    running = false;
    status.setText("Exception occurred while listening");
    status.setForeground(Color.RED);
    log.append("Listening exception: " + exception.getMessage() + "\n");
    log.append("Press Listen to restart server\n");
  }

  public void createAccountRequest(Object arg0, ConnectionToClient arg1)
  {
    // Try to create the account.
    CreateAccountData data = (CreateAccountData)arg0;
    Object result;
    if (database.createNewAccount(data.getUsername(), data.getPassword()))
    {
      result = "CreateAccountSuccessful";
      log.append("Client " + arg1.getId() + " created a new account called " + data.getUsername() + "\n");
      log.append("Client " + arg1.getId() + " joins game as " + data.getUsername() + "\n");


      Player player = new Player();
      player.setName(data.getUsername());
      player.setId((int) arg1.getId());
      game.addPlayer(player);

      playerCount.setText(game.getPlayerCount() + "/8");
    }
    else
    {
      result = new Error("The username is already in use.", "CreateAccount");
      log.append("Client " + arg1.getId() + " failed to create a new account\n");
    }

    // Send the result to the client.
    try
    {
      arg1.sendToClient(result);
    }
    catch (IOException e)
    {
      return;
    }
  }

  public void loginRequest(Object arg0, ConnectionToClient arg1)
  {
    // Check the username and password with the database.
    LoginData data = (LoginData)arg0;
    Object result;
    if (database.verifyAccount(data.getUsername(), data.getPassword()))
    {
      result = "LoginSuccessful";
      log.append("Client " + arg1.getId() + " successfully logged in as " + data.getUsername() + "\n");
      log.append("Client " + arg1.getId() + " joins game as " + data.getUsername() + "\n");

      Player player = new Player();
      player.setName(data.getUsername());
      player.setId((int) arg1.getId());
      game.addPlayer(player);

      playerCount.setText(game.getPlayerCount() + "/8");
    }
    else
    {
      result = new Error("The username and password are incorrect.", "Login");
      log.append("Client " + arg1.getId() + " failed to log in\n");
    }

    // Send the result to the client.
    try
    {
      arg1.sendToClient(result);
    }
    catch (IOException e)
    {
      return;
    }
  }

  private void dropPlayer(Object arg0, ConnectionToClient arg1)
  {
      String username = "";

      for (Player player : game.getPlayers())
      {
          if (player.getId() == (int) arg1.getId())
          {
            username = player.getName();
          }
      }

      log.append("User " + username + " has dropped from the game.\n");

      game.removePlayer((int) arg1.getId());
      playerCount.setText(game.getPlayerCount() + "/8");
  }

  public void gameStart()
  {
      game.startRound();
      InitialGameData gameStartData = new InitialGameData(game);
      sendToAllClients(gameStartData);
      log.append("Starting game.");
  }

  public Game getGame()
  {
    return game;
  }

  public void setGame(Game game)
  {
    this.game = game;
  }
}
