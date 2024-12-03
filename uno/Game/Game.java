package uno.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game implements Serializable
{
    private List<Player> playerlist;
    private Player winner;
    private Deck mainDeck;
    private Deck discardDeck;
    private String turnOrder;
    private int turn;
    private int currentPlayerIndex;
    private boolean gameOver;
    private int playerIndexIncrement;
    private Hand playable;
    private Hand dealNewHand;
    private int nextPlayer;
    private Card playedCard;
    private int winGoal;

    public Game() {
        playerlist = new ArrayList<Player>();
        mainDeck = new Deck();
        mainDeck.initialize();

        discardDeck = new Deck();
        turnOrder = "Standard";
        turn = 0;
        currentPlayerIndex = 0;
        playerIndexIncrement = 1;
        playable = new Hand();
        dealNewHand = new Hand();
        winGoal = 50;
    }

    //===============================================================================
    // Getters and Setters
    //===============================================================================

    public List<Player> getPlayers() {
        return playerlist;
    }

    public void setPlayers(List<Player> players) {
        this.playerlist = players;
    }

    public void addPlayer(Player player)
    {
        playerlist.add(player);
    }

    public void removePlayer(int id)
    {
        for (Player player : playerlist)
        {
            if (player.getId() == id)
            {
                playerlist.remove(player);
            }
        }
    }

    public Deck getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(Deck mainDeck) {
        this.mainDeck = mainDeck;
    }

    public Deck getDiscardDeck() {
        return discardDeck;
    }

    public void setDiscardDeck(Deck discardDeck) {
        this.discardDeck = discardDeck;
    }

    public String getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(String turnOrder) {
        this.turnOrder = turnOrder;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public int getPlayerIndexIncrement() {
        return playerIndexIncrement;
    }

    public void setPlayerIndexIncrement(int playerIndexIncrement) {
        this.playerIndexIncrement = playerIndexIncrement;
    }

    //===============================================================================
    //THE GAME LOGIC.
    //===============================================================================
    public void play(Player[] players) {
        //Initialize game (Add in our players)
        for (Player player : players) {
            playerlist.add(player);
        }

        //For each player, draw their initial hand (7 cards).
        for (Player player : playerlist) {
            dealNewHand = new Hand();

            for (int i = 0; i < 7; i++) {
                dealNewHand.addCard(mainDeck.draw());
            }

            player.setHand(dealNewHand);
        }

        //The game begins with the top card of the deck being placed face up in the discard pile.
        discardDeck.addToDeck(mainDeck.draw());

        //Scanner for console input (For testing logic)
        Scanner scanner = new Scanner(System.in);

        //Until a winner is reached...
        while (!gameOver) {
            //Start turn and display the current face up card (top of discard pile)
            System.out.println("===============================");
            System.out.println("Turn " + (turn + 1) + ": Player " + playerlist.get(currentPlayerIndex).getName());
            System.out.println("------");
            System.out.println("Face Up Card: " + discardDeck.getTopCard().print());


            //If the player whose turn it currently is, is not skipping their turn...
            if (!playerlist.get(currentPlayerIndex).isSkip()) {
                //Display full player hand (so they know what they have total)
                System.out.println("------");
                System.out.println("Your hand has these cards:\n");
                playerlist.get(currentPlayerIndex).getHand().printHand();
                System.out.println("------");

                //--------------------------------------------------------------
                //Determine player actions available.
                //--------------------------------------------------------------
                //Basically, we iterate through the player hand, see what is playable.
                //Add those to a "playable" hand, with an additional option for "draw" card.
                //If no cards are playable, force draw card from main deck.

                playable = new Hand();

                //For each card in the player's hand,
                for (int i = 0; i < playerlist.get(currentPlayerIndex).getHand().cardCount(); i++) {
                    //See if it is playable by comparing it to the top card of the discard pile
                    //UNO rules: If color or number of card in hand match
                    //the face up discard pile card, or is an action, it is playable.
                    if (playerlist.get(currentPlayerIndex).getHand().getCardAt(i).isPlayable(discardDeck.getTopCard())) {
                        //If the card is playable, add it to the playable hand.
                        playable.addCard(playerlist.get(currentPlayerIndex).getHand().getCardAt(i));
                    }
                }

                //Input loop initialization.
                int selectedIndex = -1;
                boolean validInput = false;

                //Show actions available.
                System.out.println("You have these actions available:\n");
                playable.printHand();

                //Print a draw card option as the default action (No playable cards, must draw a card)
                System.out.print(playable.cardCount() + 1 + ": Draw a card.\n");
                System.out.println("------\n");

                //Select action to play
                while (!validInput) {
                    System.out.print("Enter card number to play: ");
                    String input = scanner.nextLine();

                    //Accept user input as integer
                    try {
                        selectedIndex = Integer.parseInt(input);

                        //If the number falls into the available actions indexes, it is valid.
                        if (selectedIndex >= 1 && selectedIndex < playable.cardCount() + 2) {
                            validInput = true;
                        }
                        //Otherwise it falls out of those indexes, it is invalid.
                        else {
                            System.out.println("Invalid input: Please enter a number between 1 and " +
                                    ((playable.cardCount()) + 1) + ".");
                        }
                    }
                    //Must be a number to be valid.
                    catch (NumberFormatException e) {
                        System.out.println("Invalid input: Please enter a numeric value.");
                    }
                }


                //--------------------------------------------------------------
                // Process the selected card
                //--------------------------------------------------------------
                //Once a card is played, remove it from original player hand (playable hand should be reset each turn.)
                //If card is played, it is added to discard pile.
                //If that card is a special action (skip, reverse, draw 2), implement conditions.
                //If card is reverse, but only 2 players, act as skip.
                //If card is skip, the next player in the order will have skip set to true.
                //If card is draw 2, next player in order forced to draw 2.

                //Since arrays start at 0, decrease by 1.
                selectedIndex = selectedIndex - 1;

                //Player draws card from deck
                if (selectedIndex == playable.cardCount()) {
                    //Player draws a card from the deck.
                    playerlist.get(currentPlayerIndex).getHand().addCard(mainDeck.draw());

                    System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " draws 1 card.\n");

                    //Implement something here to give the player the chance to play the card if it is playable.
                } else {
                    playedCard = playable.getCardAt(selectedIndex);

                    //Player plays a standard number card
                    if (playable.getCardAt(selectedIndex).getAction() == "Number") {
                        //Remove the card from the player's hand.
                        System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + playedCard.print());

                        playerlist.get(currentPlayerIndex).getHand().removeCard(playedCard);

                        //Add it to the discard pile.
                        discardDeck.addToDeck(playedCard);
                    }

                    //Player plays a skip card
                    else if (playable.getCardAt(selectedIndex).getAction() == "Skip") {
                        //Remove the card from the player's hand.
                        System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + playedCard.print());

                        playerlist.get(currentPlayerIndex).getHand().removeCard(playedCard);

                        //Add it to the discard pile.
                        discardDeck.addToDeck(playedCard);

                        nextPlayer = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
                        playerlist.get(nextPlayer).setSkip(true);
                        System.out.println("Player " + playerlist.get(nextPlayer).getName() + " must skip their turn!\n");
                    }

                    //Player plays a reverse card
                    else if (playable.getCardAt(selectedIndex).getAction() == "Reverse") {
                        //Remove the card from the player's hand.
                        System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + playedCard.print());

                        playerlist.get(currentPlayerIndex).getHand().removeCard(playedCard);

                        //Add it to the discard pile.
                        discardDeck.addToDeck(playedCard);

                        //If only 2 players, act as a skip card
                        if (playerlist.size() < 3) {
                            nextPlayer = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
                            playerlist.get(nextPlayer).setSkip(true);

                            System.out.println("Player " + playerlist.get(nextPlayer).getName() + " must skip their turn!\n");
                        }

                        //Otherwise change turn order
                        else {
                            if (turnOrder.equals("Standard")) {
                                System.out.println("The turn order is now reversed.");
                                turnOrder = "Reverse";
                            } else if (turnOrder.equals("Reverse")) {
                                System.out.println("The turn order is now standard.");
                                turnOrder = "Standard";
                            }
                        }
                    }

                    //Player plays a draw 2 card
                    else if (playable.getCardAt(selectedIndex).getAction() == "Draw2") {
                        //Remove the card from the player's hand.
                        System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + playedCard.print());

                        playerlist.get(currentPlayerIndex).getHand().removeCard(playedCard);

                        //Add it to the discard pile.
                        discardDeck.addToDeck(playedCard);

                        //Next player in turn order must draw two cards.

                        nextPlayer = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
                        playerlist.get(nextPlayer).getHand().addCard(mainDeck.draw());
                        playerlist.get(nextPlayer).getHand().addCard(mainDeck.draw());

                        System.out.println("Player " + playerlist.get(nextPlayer).getName() + " is forced to draw 2 cards!\n");
                    }
                }


                //--------------------------------------------------------------
                //Check win conditions to see if they are met after the play.
                //--------------------------------------------------------------

                if (playerlist.get(currentPlayerIndex).getHand().cardCount() == 1) {
                    System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " calls UNO!\n");
                }

                //If player has emptied their hand,
                if (playerlist.get(currentPlayerIndex).getHand().cardCount() == 0) {
                    System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " has emptied their hand!\n");
                    //Initialize score to count.
                    int addscore = 0;

                    //For each player, count up the totals in their hand.
                    for (Player player : playerlist) {
                        //If the player is the current player, just skip them.
                        //Kinda not necessary because their hand is empty anyway...
                        if (player == playerlist.get(currentPlayerIndex)) {
                            continue;
                        }
                        //Add the player's total hand value to the score.
                        else {
                            addscore += player.getHand().getHandScore();
                        }
                    }

                    //Add to player's held score.
                    playerlist.get(currentPlayerIndex).addScore(addscore);
                    System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " adds " + addscore + " to their score.\n");
                    System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + "'s score is now " + playerlist.get(currentPlayerIndex).getScore() + ".\n");

                    //If the player's score is equal or greater than 500, they have won, the game is over.
                    if (playerlist.get(currentPlayerIndex).getScore() >= 500) {
                        //Save this to display the winner.
                        winner = playerlist.get(currentPlayerIndex);
                        gameOver = true;
                    }
                }
            }
            //If the player had to skip their turn, they get this reset.
            else {
                playerlist.get(currentPlayerIndex).setSkip(false);
            }

            // Next Turn
            turn++;

            //
            if (turnOrder.equals("Standard")) {
                playerIndexIncrement = 1;
            } else if (turnOrder.equals("Reverse")) {
                playerIndexIncrement = -1;
            }

            //This code just uses a modulo to keep all incrementations of index within the valid player list.
            currentPlayerIndex = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
        }
        scanner.close();

        System.out.println("Congratulations! Player " + playerlist.get(currentPlayerIndex).getName() + " has won.\n");
    }

    public void cardPlayed(Card card)
    {
        //Player plays a standard number card
        if (card.getAction() == "Number")
        {
            //Remove the card from the player's hand.
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + card.print());

            playerlist.get(currentPlayerIndex).getHand().removeCard(card);

            //Add it to the discard pile.
            discardDeck.addToDeck(card);
        }

        //Player plays a skip card
        else if (card.getAction() == "Skip") {
            //Remove the card from the player's hand.
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + card.print());

            playerlist.get(currentPlayerIndex).getHand().removeCard(card);

            //Add it to the discard pile.
            discardDeck.addToDeck(card);

            nextPlayer = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
            playerlist.get(nextPlayer).setSkip(true);
            System.out.println("Player " + playerlist.get(nextPlayer).getName() + " must skip their turn!\n");
        }

        //Player plays a reverse card
        else if (card.getAction() == "Reverse") {
            //Remove the card from the player's hand.
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + card.print());

            playerlist.get(currentPlayerIndex).getHand().removeCard(card);

            //Add it to the discard pile.
            discardDeck.addToDeck(card);

            //If only 2 players, act as a skip card
            if (playerlist.size() < 3) {
                nextPlayer = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
                playerlist.get(nextPlayer).setSkip(true);

                System.out.println("Player " + playerlist.get(nextPlayer).getName() + " must skip their turn!\n");
            }

            //Otherwise change turn order
            else {
                if (turnOrder.equals("Standard")) {
                    System.out.println("The turn order is now reversed.");
                    turnOrder = "Reverse";
                } else if (turnOrder.equals("Reverse")) {
                    System.out.println("The turn order is now standard.");
                    turnOrder = "Standard";
                }
            }
        }

        //Player plays a draw 2 card
        else if (card.getAction() == "Draw2") {
            //Remove the card from the player's hand.
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " plays " + card.print());

            playerlist.get(currentPlayerIndex).getHand().removeCard(card);

            //Add it to the discard pile.
            discardDeck.addToDeck(card);

            //Next player in turn order must draw two cards.

            nextPlayer = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
            draw(playerlist.get(nextPlayer));
            draw(playerlist.get(nextPlayer));

        }
    }

    public void draw(Player player)
    {
        if (mainDeck.isEmpty())
        {
            int totalDiscard = discardDeck.cardCount();

            for (int i = 0; i < totalDiscard; i++)
            {
                mainDeck.addToDeck(discardDeck.draw());
            }

            mainDeck.shuffle();
            discardDeck.addToDeck(mainDeck.draw());
            System.out.println("Decks reshuffled.");

        }
        else
        {
            System.out.println(player.getName() + " draws a card.");
            player.getHand().addCard(mainDeck.draw());
        }
    }

    public boolean winConditionMet()
    {
        //--------------------------------------------------------------
        //Check win conditions to see if they are met after the play.
        //--------------------------------------------------------------

        if (playerlist.get(currentPlayerIndex).getHand().cardCount() == 1) {
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " calls UNO!\n");
        }

        //If player has emptied their hand,
        if (playerlist.get(currentPlayerIndex).getHand().cardCount() == 0) {
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " has emptied their hand!\n");
            //Initialize score to count.
            int addscore = 0;

            //For each player, count up the totals in their hand.
            for (Player player : playerlist) {
                //If the player is the current player, just skip them.
                //Kinda not necessary because their hand is empty anyway...
                if (player == playerlist.get(currentPlayerIndex)) {
                    continue;
                }
                //Add the player's total hand value to the score.
                else {
                    addscore += player.getHand().getHandScore();
                }
            }

            //Add to player's held score.
            playerlist.get(currentPlayerIndex).addScore(addscore);
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " adds " + addscore + " to their score.\n");
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + "'s score is now " + playerlist.get(currentPlayerIndex).getScore() + ".\n");

            //If the player's score is equal or greater than 500, they have won, the game is over.
            if (playerlist.get(currentPlayerIndex).getScore() >= winGoal) {
                //Save this to display the winner.
                winner = playerlist.get(currentPlayerIndex);
                gameOver = true;
                return true;
            }
        }

        return false;
    }

    public void advanceTurn()
    {
        // Next Turn
        turn++;

        //
        if (turnOrder.equals("Standard")) {
            playerIndexIncrement = 1;
        } else if (turnOrder.equals("Reverse")) {
            playerIndexIncrement = -1;
        }

        //This code just uses a modulo to keep all incrementations of index within the valid player list.
        currentPlayerIndex = (currentPlayerIndex + playerIndexIncrement) % playerlist.size();
        getPlayers().get(currentPlayerIndex).setTurn(true);

        if (playerlist.get(currentPlayerIndex).isSkip())
        {
            System.out.println("Player " + playerlist.get(currentPlayerIndex).getName() + " skips the turn!\n");

            playerlist.get(nextPlayer).setSkip(false);

            advanceTurn();
        }
    }

    public void startRound()
    {
        //For each player, draw their initial hand (7 cards).
        for (Player player : playerlist)
        {
            dealNewHand = new Hand();

            for (int i = 0; i < 7; i++) {
                dealNewHand.addCard(mainDeck.draw());
            }

            player.setHand(dealNewHand);
        }

        //The game begins with the top card of the deck being placed face up in the discard pile.
        discardDeck.addToDeck(mainDeck.draw());
    }

    public static void main(String[] args)
    {
        Game test = new Game();
        Player[] players = new Player[2];
        players[0] = new Player("Caiden", 1);
        players[1] = new Player("Test", 1);

        test.play(players);
    }

    public void setWinGoal(int winGoal) {
        this.winGoal = winGoal;
    }

    public int getPlayerCount()
    {
        return playerlist.size();
    }
}
