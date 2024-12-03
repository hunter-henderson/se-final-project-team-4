package uno.GUI.Panels;

import uno.GUI.Controllers.GameOverControl;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel
{
    private JLabel winnerLabel;

    // Constructor for the initial panel.
    public GameOverPanel(GameOverControl goc)
    {
        // Create the information label.
        JLabel label = new JLabel("Congratulations!!", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));

        // Create the winner label.
        winnerLabel = new JLabel("DEFAULT USER" + " has won the game.");
        JPanel winnerLabelBuffer = new JPanel();
        winnerLabelBuffer.add(winnerLabel);

        // Create the return button.
        JButton returnButton = new JButton("Return to Main Menu.");
        returnButton.addActionListener(goc);
        JPanel returnButtonBuffer = new JPanel();
        returnButtonBuffer.add(returnButton);

        // Arrange the components in a grid.
        JPanel grid = new JPanel(new GridLayout(3, 1, 5, 5));
        grid.add(label);
        grid.add(winnerLabelBuffer);
        grid.add(returnButtonBuffer);
        this.add(grid);
    }

    public void setWinnerLabel(String winnerLabel)
    {
        this.winnerLabel.setText(winnerLabel);
    }
}