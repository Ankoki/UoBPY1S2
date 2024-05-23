package us.byeol.uobpyonestwo.atm.gui;

import us.byeol.uobpyonestwo.atm.api.Authorized;
import us.byeol.uobpyonestwo.atm.api.KeyPad;
import us.byeol.uobpyonestwo.atm.api.Screen;
import us.byeol.uobpyonestwo.atm.gui.screens.BaseATM;
import us.byeol.uobpyonestwo.atm.gui.screens.Login;
import us.byeol.uobpyonestwo.atm.internal.handlers.Model;
import us.byeol.uobpyonestwo.atm.misc.Debug;
import us.byeol.uobpyonestwo.atm.misc.swing.SwingFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainFrame extends JFrame {

    private static final JPanel AUTHORIZED_COMPONENTS = new JPanel(),
                                KEYPAD_COMPONENTS = new JPanel();

    static {
        // TODO add Authorized and KeyPad component things to sidebar.
        // KEYPAD
        KEYPAD_COMPONENTS.setSize(1280, 720);
        String[][] labels = {
                {"1",   "2",  "3",  "",  "Dep", "Hst"},
                {"4",   "5",  "6",  "",  "W/D", "W/D"},
                {"7",   "8",  "9",  "",  "Bal", "Fin"},
                {"CLR", "0",  "",   "",  "Ovr", "Ent"}};
        int xPosition = 100;
        int yPosition = 100;
        for (String[] row: labels) {
            for (String label : row) {
                xPosition += 120;
                if (label.isEmpty())
                    continue;
                JButton button = SwingFactory.createKeypad(label);
                button.setBounds(xPosition, yPosition, 25, 25);
                button.setVisible(true);
                KEYPAD_COMPONENTS.add(button);
            }
            yPosition += 100;
            xPosition = 100;
        }
    }

    private final Model model;

    private Screen current = null;

    /**
     * Creates the main frame that the ATM relies on.
     *
     * @param model the model to use.
     */
    public MainFrame(Model model) {
        Debug.trace("MainFrame::<constructor>(" + model + ")");
        this.model = model;
        this.setExtendedState(JFrame.NORMAL);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null); // Centres the ATM.
        this.setResizable(false); // So we can ensure our components won't be weirdly stretched by different aspect ratios.
        this.getContentPane().setBackground(new Color(127, 145, 131));
    }

    /**
     * Initiates all screens.
     */
    public void init() {
        new BaseATM(this);
        new Login(this);
    }

    /**
     * Gets the model of the frame.
     *
     * @return the model.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Shows the given screen.
     *
     * @param screen the screen to show.
     */
    public void showScreen(Screen screen) {
        boolean clear = true;
        if (screen instanceof Authorized) {
            if (!(current instanceof Authorized)) {
                this.getContentPane().removeAll();
                for (Component component : AUTHORIZED_COMPONENTS.getComponents()) {
                    component.setVisible(true);
                    screen.add((JComponent) component);
                }
            } else {
                for (Component component : this.getContentPane().getComponents())
                    if (component != AUTHORIZED_COMPONENTS)
                        this.getContentPane().remove(component);
            }
            clear = false;
        }
        if (screen instanceof KeyPad) {
            if (!(current instanceof KeyPad)) {
                this.getContentPane().removeAll();
                screen.add(KEYPAD_COMPONENTS);
            } else {
                for (Component component : this.getContentPane().getComponents())
                    if (component != KEYPAD_COMPONENTS)
                        this.getContentPane().remove(component);
            }
            clear = false;
        }
        if (clear)
            this.getContentPane().removeAll();
        this.repaint();
        for (JComponent component : screen)
            this.getContentPane().add(component);
        this.revalidate();
        this.setVisible(true);
        this.current = screen;
        Debug.trace("The screen '" + screen.getClass().getSimpleName() + "' has been shown.");
    }

    /**
     * Processes the given action for the model.
     */
    public void processInteraction(String action) {
        Debug.trace("Controller::process: action[" + action + "]");
        if (Arrays.asList("1234567890".split("")).contains(action)) {
            model.processNumber(action);
            return;
        }
        switch (action) {
            case "CLR" -> model.processClear();
            case "Ent" -> model.processEnter();
            case "W/D" -> model.processWithdraw();
            case "W/D B" -> model.processWithdrawBal();
            case "Hst" -> model.processAccountHistory();
            case "Dep" -> model.processDeposit();
            case "Bal" -> model.processBalance();
            case "Ovr" -> model.processOverdraftChange();
            case "Fin" -> model.processFinish();
            default -> model.processUnknownKey(action);
        }
    }

}
