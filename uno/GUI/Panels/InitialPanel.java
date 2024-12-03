package uno.GUI.Panels;

import uno.GUI.Controllers.InitialControl;

import java.awt.*;
import javax.swing.*;

public class InitialPanel extends JPanel
{
  // Constructor for the initial panel.
  public InitialPanel(InitialControl ic)
  {
    // Create the controller.
    //InitialControl controller = new InitialControl(container);

    // Create the information label.
    JLabel label = new JLabel("UNO!", JLabel.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 30));

    JLabel username = new JLabel("Log in or create an account.");

    JPanel introPanel = new JPanel();
    introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
    introPanel.add(label);
    introPanel.add(username);

    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    username.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Create the login button.
    JButton loginButton = new JButton("Login");
    loginButton.addActionListener(ic);
    JPanel loginButtonBuffer = new JPanel();
    loginButtonBuffer.add(loginButton);
    
    // Create the create account button.
    JButton createButton = new JButton("Create");
    createButton.addActionListener(ic);
    JPanel createButtonBuffer = new JPanel();
    createButtonBuffer.add(createButton);

    // Create the return to main menu button.
    JButton mainmenuButton = new JButton("Return to Main Menu.");
    mainmenuButton.addActionListener(ic);
    JPanel mainmenuBuffer = new JPanel();
    mainmenuBuffer.add(mainmenuButton);

    // Arrange the components in a grid.
    JPanel grid = new JPanel(new GridLayout(4, 1, 5, 5));
    grid.add(introPanel);
    grid.add(loginButtonBuffer);
    grid.add(createButtonBuffer);
    grid.add(mainmenuBuffer);
    this.add(grid);
  }
}
