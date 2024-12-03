package uno.GUI.Panels;

import uno.GUI.Controllers.GameControl;
import uno.Game.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardPanel extends JPanel
{
    private JLabel cardPlayable;
    private Card card;
    private JButton cardButton;
    private int id;


    public CardPanel(Card inputcard, GameControl gc, int idnum)
    {
        //Constructor stuff.
        this.card = inputcard;
        this.id= idnum;
        cardPlayable = new JLabel("-");

        //Construct a card as a button.
        cardButton = new JButton();
        cardButton.addActionListener(gc);
        cardButton.setFocusPainted(false);

        //Set card image and scale it down.
        String cardFilename = card.getImage();
        ImageIcon cardIcon = new ImageIcon(cardFilename);
        Image scaledCard = cardIcon.getImage().getScaledInstance(60, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledCardIcon = new ImageIcon(scaledCard);

        //Set the image to the button.
        cardButton.setIcon(scaledCardIcon);

        // Set the ActionCommand to output card name.
        cardButton.setActionCommand("PlayCard:" + id);

        //Add card button and the playable state of the card.
        JPanel cardBuffer = new JPanel();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(cardPlayable);
        this.add(cardButton);

        cardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPlayable.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public Card getCard()
    {
        return card;
    }

    public void setCard(Card card)
    {
        this.card = card;
    }

    public void setCardPlayable(boolean playable)
    {
        if (playable)
        {
            cardPlayable.setText("PLAY");
        }
        else
        {
            cardPlayable.setText("---");
        }
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
