package uno.Game;

import ocsf.client.AbstractClient;
import uno.GUI.Controllers.CreateAccountControl;
import uno.GUI.Controllers.GameControl;
import uno.GUI.Controllers.LoginControl;
import uno.GUI.Controllers.WaitingControl;

import javax.swing.*;

public class GameClient extends AbstractClient
{
  // Private data fields for storing the GUI controllers.
  private LoginControl loginControl;
  private CreateAccountControl createAccountControl;
  private WaitingControl waitingControl;
  private GameControl gameControl;
  private Player player;
  private JTextArea log;

  // Setters for the GUI controllers.
  public void setLoginControl(LoginControl loginControl)
  {
    this.loginControl = loginControl;
  }
  public void setCreateAccountControl(CreateAccountControl createAccountControl) { this.createAccountControl = createAccountControl; }
  public void setWaitingControl(WaitingControl waitingControl) { this.waitingControl = waitingControl; }
  public void setGameControl(GameControl gameControl) { this.gameControl = gameControl; }

  //Log setter.
  public void setLog(JTextArea log)
  {
    this.log = log;
  }

  // Constructor for initializing the client with default settings.
  public GameClient()
  {
    super("localhost", 8300);
    player = new Player();
    player.setName("Default");
  }

  public void setPlayer(Player player){ this.player = player; }
  public Player getPlayer(){ return player; }
  
  // Method that handles messages from the server.
  public void handleMessageFromServer(Object arg0)
  {
    // If we received a String, figure out what this event is.
    if (arg0 instanceof String)
    {
      // Get the text of the message.
      String message = (String)arg0;
      
      // If we successfully logged in, tell the login controller.
      if (message.equals("LoginSuccessful"))
      {
        loginControl.loginSuccess();
      }
      
      // If we successfully created an account, tell the create account controller.
      else if (message.equals("CreateAccountSuccessful"))
      {
        createAccountControl.createAccountSuccess();
      }

      // DEBUG
      else if (message.equals("TestMessage"))
      {
        System.out.println("Test Message received for " + player.getName());
      }
    }

    //If we've received a game object, that means a turn has been made and game info has been updated.
    else if (arg0 instanceof Game)
    {
      Game game = (Game)arg0;
      gameControl.setGame(game);
      gameControl.gameUpdate();
    }

    //If we received a player object, that means the game is over, and the object represents the winner.
    else if (arg0 instanceof Player)
    {
      Player winningplayer = (Player)arg0;
      gameControl.setWinner(winningplayer);
      gameControl.gameOver();
    }

    // If host clicks to start game, start the game.
    else if (arg0 instanceof InitialGameData)
    {
        InitialGameData startgame = (InitialGameData) arg0;
        gameControl.setGame(startgame.game);
        gameControl.initializePlayer();

        waitingControl.gameStarted();
    }

    // If we received an Error, figure out where to display it.
    else if (arg0 instanceof Error)
    {
      // Get the Error object.
      Error error = (Error)arg0;
      
      // Display login errors using the login controller.
      if (error.getType().equals("Login"))
      {
        loginControl.displayError(error.getMessage());
      }
      
      // Display account creation errors using the create account controller.
      else if (error.getType().equals("CreateAccount"))
      {
        createAccountControl.displayError(error.getMessage());
      }
    }
  }  
}
