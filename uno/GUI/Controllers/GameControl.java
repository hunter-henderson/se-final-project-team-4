package uno.GUI.Controllers;

import uno.Game.GameClient;
import uno.Game.GameServer;
import uno.Game.Game;
import uno.Game.Player;
import uno.GUI.Panels.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class GameControl implements ActionListener
{
    // Private data field for storing the container.
    private JPanel container;
    private GameClient client;
    private GameServer server;
    private Game game;
    private Player winner;
    private JTextArea gameLog;
    private boolean isYourTurn;

    // Constructor for the initial controller.
    public GameControl(JPanel container, GameClient client)
    {
        this.container = container;
        this.client = client;
        this.game = new Game();
        this.winner = null;
        this.isYourTurn = false;

        //Default stuff. Get rid of later.
        Player[] players = new Player[2];
        players[0] = new Player("Caiden", 1);
        players[1] = new Player("Test", 1);

        game.setPlayers(List.of(players));

        game.startRound();
    }

    public Game getGame()
    {
        return game;
    }
    public void setGame(Game game)
    {
        this.game = game;
    }

    public void setWinner(Player winner)
    {
        this.winner = winner;
    }


    // Handle button clicks.
    public void actionPerformed(ActionEvent ae)
    {
        // Get the name of the button clicked.
        String command = ae.getActionCommand();

        // Draw Card Button draws a card.
        if (command.contains("PlayCard:"))
        {
            if (isYourTurn) {
                String playedCardId = command.substring("PlayCard:".length());

                if (game.getPlayers().get(game.getCurrentPlayerIndex()).getHand().getCardAt(Integer.parseInt(playedCardId)).isPlayable(game.getDiscardDeck().getTopCard())) {
                    try {
                        client.sendToServer(game.getPlayers().get(game.getCurrentPlayerIndex()).getHand().getCardAt(Integer.parseInt(playedCardId)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        else if (command.equals("Draw Card"))
        {
            if (isYourTurn)
            {
                try
                {
                    client.sendToServer("DrawCard");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // After a play, update the game screen.
    public void gameUpdate()
    {
        for (Player player : game.getPlayers())
        {
            if (player.getId() == client.getPlayer().getId())
            {
                isYourTurn = player.isTurn();
            }
        }

        container.remove(6);
        container.add(new GamePanel(this), "Game");
        container.repaint();
        container.revalidate();

        CardLayout cardLayout = (CardLayout) container.getLayout();
        cardLayout.show(container, "Game");
    }

    public void gameOver()
    {
        GameOverPanel gameOverPanel = (GameOverPanel) container.getComponent(5);
        gameOverPanel.setWinnerLabel(winner.getName() + " has won the game.");

        CardLayout cardLayout = (CardLayout) container.getLayout();
        cardLayout.show(container, "GameOver");
    }
}
