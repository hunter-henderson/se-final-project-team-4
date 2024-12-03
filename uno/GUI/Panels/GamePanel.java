package uno.GUI.Panels;

import uno.GUI.Controllers.GameControl;
import uno.Game.Deck;
import uno.Game.Game;
import uno.Game.Hand;
import uno.Game.Player;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel
{
  private int cardX = 60;
  private int cardY = 100;
  private List<CardPanel> cardPanels;
  private JTextArea gameLog;
  private Game game;
  private Player player;


  // Constructor for the main menu.
  public GamePanel(GameControl gc, Player player)
  {
    cardPanels = new ArrayList<CardPanel>();
    this.game = gc.getGame();
    this.player = player;

    String clientPlayerName = player.getName();
    for (Player checkplayer : game.getPlayers())
    {
      String compareName = checkplayer.getName();

      if (compareName.equals(clientPlayerName))
      {
          this.player = checkplayer;
          break;
      }
    }

    this.setLayout(new BorderLayout());
    //----------------------------------------------------------------------------------------------------------------
    // Display player information (Name, Score)
    //----------------------------------------------------------------------------------------------------------------
    JLabel playerTurn = new JLabel("Player " + game.getPlayers().get(game.getCurrentPlayerIndex()).getName() + "'s Turn");
    JLabel playerName = new JLabel(this.player.getName() + "'s Hand");
    playerName.setFont(new Font("Arial", Font.BOLD, 20));
    JLabel playerScore = new JLabel("Score: " + this.player.getScore());
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

    infoPanel.add(playerTurn);
    infoPanel.add(playerName);
    infoPanel.add(playerScore);

    playerTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
    playerName.setAlignmentX(Component.CENTER_ALIGNMENT);
    playerScore.setAlignmentX(Component.CENTER_ALIGNMENT);


    //----------------------------------------------------------------------------------------------------------------
    // Create the game log.
    //----------------------------------------------------------------------------------------------------------------
    JPanel log = new JPanel();
    gameLog = new JTextArea(7,30);
    gameLog.setEditable(false);

    JScrollPane gameLogScroller = new JScrollPane(gameLog);
    gameLogScroller.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

    log.add(gameLogScroller);

    Dimension logSize = new Dimension(1000, 100);
    gameLog.setPreferredSize(logSize);
    gameLog.setMinimumSize(logSize);
    gameLog.setMaximumSize(logSize);


    //----------------------------------------------------------------------------------------------------------------
    // Create the two decks to be displayed.
    //----------------------------------------------------------------------------------------------------------------
    Dimension sceneSize = new Dimension(350, 100);

    //Main Deck (This is mostly for decoration).
    JPanel deckPanel = new JPanel();

    deckPanel.setPreferredSize(sceneSize);
    deckPanel.setMinimumSize(sceneSize);
    deckPanel.setMaximumSize(sceneSize);

    String deckCardPath = "src/uno/CardImages/uno.png";
    ImageIcon deckCardIcon = new ImageIcon(deckCardPath);
    Image scaledDeckCard = deckCardIcon.getImage().getScaledInstance(cardX, cardY, Image.SCALE_SMOOTH);
    ImageIcon scaledDeckCardIcon = new ImageIcon(scaledDeckCard);

    JLabel deckCardImage = new JLabel();
    deckCardImage.setIcon(scaledDeckCardIcon);

    JPanel deckInfoPanel = new JPanel();
    deckInfoPanel.setLayout(new BoxLayout(deckInfoPanel, BoxLayout.Y_AXIS));
    JLabel deckLabel = new JLabel("DECK");
    JLabel deckCount = new JLabel(game.getMainDeck().cardCount() + " Cards");

    deckInfoPanel.add(deckLabel);
    deckInfoPanel.add(deckCount);


    //Discard Pile
    JPanel discardPanel = new JPanel();

    discardPanel.setPreferredSize(sceneSize);
    discardPanel.setMinimumSize(sceneSize);
    discardPanel.setMaximumSize(sceneSize);

    String discardCardPath = game.getDiscardDeck().getTopCard().getImage();
    ImageIcon discardCardIcon = new ImageIcon(discardCardPath);
    Image scaledDiscardCard = discardCardIcon.getImage().getScaledInstance(cardX, cardY, Image.SCALE_SMOOTH);
    ImageIcon scaledDiscardCardIcon = new ImageIcon(scaledDiscardCard);

    JLabel discardCardImage = new JLabel();
    discardCardImage.setIcon(scaledDiscardCardIcon);

    JPanel discardInfoPanel = new JPanel();
    discardInfoPanel.setLayout(new BoxLayout(discardInfoPanel, BoxLayout.Y_AXIS));
    JLabel discardLabel = new JLabel("DISCARD");
    JLabel discardCount = new JLabel(game.getDiscardDeck().cardCount()+ " Cards");

    discardInfoPanel.add(discardLabel);
    discardInfoPanel.add(discardCount);



    //Add to scene.
    deckPanel.add(deckInfoPanel);
    deckPanel.add(deckCardImage);

    discardPanel.add(discardCardImage);
    discardPanel.add(discardInfoPanel);


    deckPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    discardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel decks = new JPanel();
    decks.add(deckPanel);
    decks.add(discardPanel);


    //----------------------------------------------------------------------------------------------------------------
    //Create cards display panel.
    //----------------------------------------------------------------------------------------------------------------
    JPanel cards = new JPanel();
    cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));

    JLabel isYourTurn = new JLabel("");
    isYourTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
    if (this.player.isTurn())
    {
      isYourTurn.setText("It's your turn! Play a card.");
    }
    else
    {
      isYourTurn.setText("It's not your turn! Please wait.");
    }
    cards.add(isYourTurn);

    JPanel cardsPanel = new JPanel(); // Panel to hold the card components
    cardsPanel.setLayout(new GridBagLayout());


    // Set a fixed size for cardsPanel to keep the scroll area consistent
    Dimension cardsPanelSize = new Dimension(1000, 150);
    cardsPanel.setPreferredSize(cardsPanelSize);
    cardsPanel.setMinimumSize(cardsPanelSize);
    cardsPanel.setMaximumSize(cardsPanelSize);

    JScrollPane cardsScroller = new JScrollPane(cardsPanel);
    cardsScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    cardsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    // Set a fixed size for the scroll pane
    cardsScroller.setPreferredSize(cardsPanelSize);
    cardsScroller.setMinimumSize(cardsPanelSize);
    cardsScroller.setMaximumSize(cardsPanelSize);

    cards.add(cardsScroller);
    cardsScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

    //----------------------------------------------------------------------------------------------------------------
    //Add cards to card panel.
    //----------------------------------------------------------------------------------------------------------------
    for (int i = 0; i < this.player.getHand().cardCount(); i++)
    {
      cardPanels.add(new CardPanel(this.player.getHand().getCardAt(i), gc, i));
    }

    for (int i = 0; i < this.player.getHand().cardCount(); i++)
    {
      cardsPanel.add(cardPanels.get(i));
      cardPanels.get(i).setAlignmentX(Component.CENTER_ALIGNMENT);

      if (cardPanels.get(i).getCard().isPlayable(game.getDiscardDeck().getTopCard()))
      {
        cardPanels.get(i).setCardPlayable(true);
      }
    }

    JButton drawCardButton = new JButton("Draw Card");
    drawCardButton.addActionListener(gc);

    cards.add(drawCardButton);
    drawCardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    //----------------------------------------------------------------------------------------------------------------
    // Create a spacer between the decks and hand.
    //----------------------------------------------------------------------------------------------------------------
    JPanel spacer = new JPanel();
    spacer.setPreferredSize(new Dimension(0, 20));
    spacer.setOpaque(false);

    //----------------------------------------------------------------------------------------------------------------
    // Arrange all components in a grid.
    //----------------------------------------------------------------------------------------------------------------
    JPanel mainGrid = new JPanel();
    mainGrid.setLayout(new BoxLayout(mainGrid, BoxLayout.Y_AXIS));
    mainGrid.add(infoPanel);
    mainGrid.add(log);
    mainGrid.add(decks);
    mainGrid.add(spacer);
    mainGrid.add(cards);


    //Align everything in main grid.
    infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    log.setAlignmentX(Component.CENTER_ALIGNMENT);
    decks.setAlignmentX(Component.CENTER_ALIGNMENT);
    cards.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.add(mainGrid);
  }

  public void setGameLog(JTextArea gameLog)
  {
    this.gameLog = gameLog;
  }
  public JTextArea getGameLog()
  {
    return gameLog;
  }
  public Game getGame()
  {
    return game;
  }
  public void setGame(Game game)
  {
    this.game = game;
  }
  public Player getPlayer()
  {
    return player;
  }
  public void setPlayer(Player player)
  {
    this.player = player;
  }
}
