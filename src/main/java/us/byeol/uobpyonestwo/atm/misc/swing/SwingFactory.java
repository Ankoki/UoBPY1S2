package us.byeol.uobpyonestwo.atm.misc.swing;

import us.byeol.uobpyonestwo.atm.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SwingFactory {

    /**
     * Creates a button for a keypad.
     *
     * @param number the number of the keypad number.
     * @return the created button.
     */
    public static JButton createKeypad(String number) {
        JButton button = new JButton(String.valueOf(number));
        button.setSize(100, 100);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 8, 8, 8));
        button.setIcon(null);
        button.addActionListener(event -> Main
                                        .getMainFrame()
                                        .processInteraction(number));
        return button;
    }

}
