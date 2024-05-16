package us.byeol.uobpyonestwo.gui;

import us.byeol.uobpyonestwo.api.Authorized;
import us.byeol.uobpyonestwo.api.Screen;
import us.byeol.uobpyonestwo.gui.screens.BaseATM;
import us.byeol.uobpyonestwo.gui.screens.Login;
import us.byeol.uobpyonestwo.misc.Debug;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final JPanel AUTHORIZED_COMPONENTS = new JPanel();

    static {
        // TODO add Authorized component things to sidebar.
    }

    private Screen current = null;

    /**
     * Creates the main frame that the ATM relies on.
     */
    public MainFrame() {
        Debug.trace("MainFrame::<constructor>");
        this.setExtendedState(JFrame.NORMAL);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null); // Centres the ATM.
        this.setResizable(false); // So we can ensure our components won't be weirdly stretched by different aspect ratios.
        this.getContentPane().setBackground(new Color(127, 145, 131));
        this.init();
    }

    public void init() {
        new BaseATM(this);
        new Login(this);
    }

    /**
     * Shows the given screen.
     *
     * @param screen the screen to show.
     */
    public void showScreen(Screen screen) {
        if (screen instanceof Authorized) {
            if (!(current instanceof Authorized)) {
                this.getContentPane().removeAll();
                screen.add(AUTHORIZED_COMPONENTS);
            } else {
                for (Component component : this.getContentPane().getComponents())
                    if (component != AUTHORIZED_COMPONENTS)
                        this.getContentPane().remove(component);
            }
        } else
            this.getContentPane().removeAll();
        this.repaint();
        for (JComponent component : screen)
            this.getContentPane().add(component);
        this.revalidate();
        this.setVisible(true);
        this.current = screen;
        Debug.trace("The screen '" + screen.getClass().getSimpleName() + "' has been shown.");
    }

}
