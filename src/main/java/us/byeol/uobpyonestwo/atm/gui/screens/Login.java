package us.byeol.uobpyonestwo.atm.gui.screens;

import us.byeol.uobpyonestwo.atm.Main;
import us.byeol.uobpyonestwo.atm.api.KeyPad;
import us.byeol.uobpyonestwo.atm.api.Screen;

import javax.swing.*;
import java.awt.*;

// Colour Scheme:
// TEXT : RGB(43,43,43)
public class Login extends Screen implements KeyPad {

    private final JLabel firstDisplay, secondDisplay;
    private final JTextField accountNumber;
    private final JPasswordField password;

    public Login(JFrame frame) {
        super(frame);
        // Title
        JLabel title = new JLabel(": ATM :");
        title.setFont(new Font("Monospaced", Font.PLAIN, 25));
        title.setForeground(new Color(43, 43, 43));
        title.setBounds(600, -75, 800, 300);
        this.add(title);
        // First Display
        this.firstDisplay = new JLabel(Main.getMainFrame().getModel().getFirstDisplay());
        firstDisplay.setFont(new Font("Monospaced", Font.PLAIN, 25));
        firstDisplay.setForeground(new Color(43, 43, 43));
        firstDisplay.setBounds(600, -25, 800, 300);
        // Second Display
        this.secondDisplay = new JLabel(Main.getMainFrame().getModel().getSecondDisplay());
        secondDisplay.setFont(new Font("Monospaced", Font.PLAIN, 25));
        secondDisplay.setForeground(new Color(43, 43, 43));
        secondDisplay.setBounds(600, -25, 800, 300);
        // Account Number
        this.accountNumber = new JTextField("Account Number");
        accountNumber.setHorizontalAlignment(JTextField.CENTER);
        accountNumber.setForeground(new Color(255, 255, 255));
        accountNumber.setBackground(new Color(75, 87, 78));
        accountNumber.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        accountNumber.setBounds(550, 300, 175, 50);
        this.add(accountNumber);
        // Password
        this.password = new JPasswordField("Account Password");
        password.setHorizontalAlignment(JTextField.CENTER);
        password.setForeground(new Color(255, 255, 255));
        password.setBackground(new Color(75, 87, 78));
        password.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        password.setBounds(600, 750, 175, 50);
        this.add(password);
    }

    public void setFirstDisplay(String content) {
        this.firstDisplay.setText(content);
    }

    public void setSecondDisplay(String content) {
        this.secondDisplay.setText(content);
    }

    public void setAccountNumber(String content) {
        this.accountNumber.setText(content);
    }

    public void setPassword(String content) {
        this.password.setText(content);
    }

    public void focusAccountNumber() {
        this.accountNumber.requestFocus();
    }

    public void focusPassword() {
        this.password.requestFocus();
    }

}
