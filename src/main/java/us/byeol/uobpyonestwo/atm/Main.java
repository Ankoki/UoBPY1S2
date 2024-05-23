package us.byeol.uobpyonestwo.atm;

import us.byeol.uobpyonestwo.atm.api.Screen;
import us.byeol.uobpyonestwo.atm.gui.MainFrame;
import us.byeol.uobpyonestwo.atm.gui.screens.Login;
import us.byeol.uobpyonestwo.atm.internal.Bank;
import us.byeol.uobpyonestwo.atm.internal.handlers.Model;
import us.byeol.uobpyonestwo.atm.misc.Debug;

/**
 * Main class which handles launching the ATM program.
 */
public class Main {

    private static MainFrame mainFrame;

    public static MainFrame getMainFrame() {
        return Main.mainFrame;
    }

    public static void main(String[] args) {
        new Main().start();
    }

    /**
     * Starts the ATM with the given window.
     */
    public void start() {
        Debug.set(true);
        Debug.trace("ATM starting...");
        Debug.trace("Main::start");

        Bank bank = new Bank();
        bank.addBankAccount(10001, 11111, 100);
        bank.addBankAccount(10002, 22222, 50);

        Main.mainFrame = new MainFrame(new Model(bank));
        Main.mainFrame.init();
        mainFrame.showScreen(Screen.getScreen(Login.class));

        Debug.trace("ATM running...");
    }

}
