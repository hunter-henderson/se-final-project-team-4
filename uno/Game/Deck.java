package uno.Game;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck implements Serializable
{
    private Stack<Card> cards;

    public Deck()
    {
        cards = new Stack<Card>();
    }

    public void initialize()
    {
        Card[] cardsInit = new Card[25];
        //Example: "src/CardImages/Red0.png"
        String filepath = "src/uno/CardImages/";
        String color = "Red";
        String filename;
        String action = "Number";

        for (int j = 0; j < 4; j++)
        {
            if (j == 1)
            {
                color = "Blue";
            }
            else if (j == 2)
            {
                color = "Green";
            }
            else if (j == 3)
            {
                color = "Yellow";
            }

            for (int i = 0; i < 25; i++)
            {
                int index = i;

                if (i > 12)
                {
                    index = i - 12;
                }

                if (index == 10)
                {
                    filename = filepath + color + "Skip.png";
                    action = "Skip";
                    index = 20;
                }
                else if (index == 11)
                {
                    filename = filepath + color + "Reverse.png";
                    action = "Reverse";
                    index = 20;
                }
                else if (index == 12)
                {
                    filename = filepath + color + "Draw2.png";
                    action = "Draw2";
                    index = 20;
                }
                else
                {
                    filename = filepath + color + index + ".png";
                    action = "Number";
                }

                cardsInit[i] = new Card(filename, color, index, action);
            }

            for(Card card : cardsInit)
            {
                cards.push(card);
            }
        }

        this.shuffle();
    }

    public void empty()
    {
        cards.clear();
    }

    public void shuffle()
    {
        // Convert stack to list
        List<Card> shufflecards = new java.util.ArrayList<>(cards);

        // Shuffle the list
        Collections.shuffle(shufflecards);

        // Clear the stack
        cards.clear();

        // Push shuffled elements back to the stack
        for (Card card : shufflecards)
        {
            cards.push(card);
        }
    }

    public Card getTopCard()
    {
        return cards.peek();
    }

    public void addToDeck(Card card)
    {
        this.cards.push(card);
    }

    public Card draw()
    {
        Card card = cards.pop();
        return card;
    }

    public void print()
    {
        for (Card card : cards)
        {
            System.out.println(card.getImage() + "," + card.getColor() + "," + card.getValue() + "\n");
        }

        System.out.println(cards.size());
    }

    public int cardCount()
    {
        return cards.size();
    }

    public boolean isEmpty()
    {
        return cards.isEmpty();
    }
    public static void main(String[] args)
    {
        Deck test = new Deck();
        test.initialize();
        test.print();
    }


}
