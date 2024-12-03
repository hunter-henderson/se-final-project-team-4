package uno.Game;

import java.io.Serializable;

public class Player implements Serializable
{
    private String name;
    private Hand hand;
    private int score;
    private int id;
    private boolean skip;
    private boolean isTurn;

    public Player()
    {
        this.name = "";
        this.hand = new Hand();
        this.score = 0;
        this.id = 0;
        this.skip = false;
    }

    public Player(String name, int id)
    {
        this.name = name;
        this.hand = new Hand();
        this.score = 0;
        this.id = id;
        this.skip = false;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Hand getHand()
    {
        return hand;
    }

    public void setHand(Hand hand)
    {
        this.hand = hand;
    }

    public int getScore()
    {
        return score;
    }

    public void addScore(int score)
    {
        this.score = this.score + score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }
}
