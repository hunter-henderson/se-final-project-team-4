package uno.GUI.Panels;

import uno.GUI.Controllers.GameOverControl;
import uno.GUI.Controllers.MainMenuControl;
import uno.GUI.Controllers.WaitingControl;
import uno.ServerGUI;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel
{
    private JLabel playerWaitingLabel;
    private int playerCount;
    private JLabel hostingOrJoining;
    private JLabel status;
    private int port;
    private boolean host;

    // Constructor for the initial panel.
    public WaitingPanel(WaitingControl wc)
    {
        port = wc.getClient().getPort();

        // Create the information label.
        JLabel label = new JLabel("UNO!", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));

        hostingOrJoining = new JLabel("");

        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.add(label);
        introPanel.add(hostingOrJoining);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        hostingOrJoining.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the waiting count label.
        playerWaitingLabel = new JLabel("Waiting for host to start game.");
        JPanel playerWaitingLabelBuffer = new JPanel();
        playerWaitingLabelBuffer.add(playerWaitingLabel);

        // Create the hosting/joining status.
        status = new JLabel("", JLabel.CENTER);


        status.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel statusBuffer = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.add(status);


        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        hostingOrJoining.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the return button.
        JButton returnButton = new JButton("Return to Main Menu.");
        returnButton.addActionListener(wc);
        JPanel returnButtonBuffer = new JPanel();
        returnButtonBuffer.add(returnButton);

        // Arrange the components in a grid.
        JPanel grid = new JPanel(new GridLayout(4, 1, 5, 5));
        grid.add(introPanel);
        grid.add(playerWaitingLabelBuffer);
        grid.add(statusBuffer);
        grid.add(returnButtonBuffer);
        this.add(grid);
    }


    public void isHosting(boolean hosting)
    {
        host = hosting;

        if (hosting)
        {
            hostingOrJoining.setText("Hosting game on Port " + port);
        }
        else if (!hosting)
        {
            hostingOrJoining.setText("Joining game on Port " + port);
        }
    }

    public void setStatus(String status)
    {
        this.status.setText(status);
    }

    public boolean hosting()
    {
        return host;
    }
}
