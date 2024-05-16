package us.byeol.uobpyonestwo.gui.screens;

import us.byeol.uobpyonestwo.api.Screen;
import us.byeol.uobpyonestwo.misc.swing.JPromptPasswordField;
import us.byeol.uobpyonestwo.misc.swing.JPromptTextField;

import javax.swing.*;
import java.awt.*;

// Colour Scheme:
// TEXT : RGB(43,43,43)
public class Login extends Screen {

    public Login(JFrame frame) {
        super(frame);
        // Title
        JLabel title = new JLabel("· ATM  ·");
        title.setFont(new Font("Monospaced", Font.PLAIN, 25));
        title.setForeground(new Color(43, 43, 43));
        title.setBounds(475, 50, 800, 300);
        this.add(title);
        // Account Number
        JTextField accountNumber = new JTextField("Account Number");
        accountNumber.setHorizontalAlignment(JTextField.CENTER);
        accountNumber.setForeground(new Color(255, 255, 255));
        accountNumber.setBackground(new Color(75, 87, 78));
        accountNumber.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        accountNumber.setBounds(625, 400, 175, 50);
        this.add(accountNumber);
        // Password
        JPasswordField password = new JPasswordField("Account Password");
        password.setHorizontalAlignment(JTextField.CENTER);
        password.setForeground(new Color(255, 255, 255));
        password.setBackground(new Color(75, 87, 78));
        accountNumber.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        password.setBounds(625, 465, 175, 50);
        this.add(password);
    }

}
