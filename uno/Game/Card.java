package uno.Game;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable
{

    private String image;
    private String color;
    private int value;
    private String action;

    public Card(String image, String color, int value, String action)
    {
        this.image = image;
        this.color = color;
        this.value = value;
        this.action = action;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    public boolean isPlayable(Card faceUp)
    {
        // Use equals() for string comparison
        if(((this.getValue() == faceUp.getValue()) && this.getAction().equals("Number"))
                || this.getColor().equals(faceUp.getColor())
                || ((this.getAction().equals(faceUp.getAction())) && this.getValue() > 9))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String print()
    {
        // Get the file name (e.g., "GreenSkip.png")
        String cardname = image.substring(image.lastIndexOf('/') + 1);

        // Remove the file extension (e.g., "GreenSkip")
        cardname = cardname.substring(0, cardname.lastIndexOf('.'));

        // Split color and value using regex (e.g., "Green", "Skip")
        String value = cardname.replaceFirst(color, "").trim();

        cardname = color + " " + value;

        return cardname;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return value == card.value &&
                Objects.equals(image, card.image) &&
                Objects.equals(color, card.color) &&
                Objects.equals(action, card.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, color, value, action);
    }
}
