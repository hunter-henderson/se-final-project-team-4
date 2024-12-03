package uno.GUI.Panels;

import uno.GUI.Controllers.MainMenuControl;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel
{
    // Constructor for the initial panel.
    public MainMenuPanel(MainMenuControl mmc)
    {
        // Create the information label.
        JLabel label = new JLabel("UNO!", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel username = new JLabel("Please choose an option.");

        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.add(label);
        introPanel.add(username);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the host game button.
        JButton hostGameButton = new JButton("Host Game");
        hostGameButton.addActionListener(mmc);
        JPanel hostGameButtonBuffer = new JPanel();
        hostGameButtonBuffer.add(hostGameButton);

        // Create the join game button.
        JButton joinGameButton = new JButton("Join Game");
        joinGameButton.addActionListener(mmc);
        JPanel joinGameButtonBuffer = new JPanel();
        joinGameButtonBuffer.add(joinGameButton);

        // Create the log out button.
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(mmc);
        JPanel logoutButtonBuffer = new JPanel();
        logoutButtonBuffer.add(logoutButton);

        // Arrange the components in a grid.
        JPanel grid = new JPanel(new GridLayout(4, 1, 5, 5));
        grid.add(introPanel);
        grid.add(hostGameButtonBuffer);
        grid.add(joinGameButtonBuffer);
        grid.add(logoutButtonBuffer);
        this.add(grid);
    }
}
