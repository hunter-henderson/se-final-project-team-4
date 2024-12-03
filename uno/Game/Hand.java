package uno.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hand implements Serializable
{
    private List<Card> cards;

    public Hand()
    {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card)
    {
        this.cards.add(card);
    }

    public void removeCard(Card card)
    {
        this.cards.remove(card);
    }

    public Card getCardAt(int index)
    {
        return this.cards.get(index);
    }

    public int cardCount()
    {
        return this.cards.size();
    }

    public int getHandScore()
    {
        int score = 0;

        for (Card card : cards)
        {
            score += card.getValue();
        }

        return score;
    }

    public void printHand()
    {

        int i = 1;
        for (Card card : cards)
        {
            System.out.println(i + ": " + card.print());
            i++;
        }
    }

    public boolean isEmpty()
    {
        return this.cards.isEmpty();
    }
}
