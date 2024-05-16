package us.byeol.uobpyonestwo;

import javafx.application.Application;
import javafx.stage.Stage;
import us.byeol.uobpyonestwo.api.Screen;
import us.byeol.uobpyonestwo.gui.MainFrame;
import us.byeol.uobpyonestwo.gui.screens.BaseATM;
import us.byeol.uobpyonestwo.gui.screens.Login;
import us.byeol.uobpyonestwo.internal.Bank;
import us.byeol.uobpyonestwo.internal.handlers.Controller;
import us.byeol.uobpyonestwo.internal.handlers.Model;
import us.byeol.uobpyonestwo.internal.handlers.View;
import us.byeol.uobpyonestwo.misc.Debug;

import java.util.List;
import java.util.ArrayList;

/**
 * Main class which handles launching the ATM program.
 */
public class Main extends Application {

    private static MainFrame mainFrame;

    public static MainFrame getMainFrame() {
        return Main.mainFrame;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Starts the ATM with the given window.
     *
     * @param window to start this ATM on.
     */
    @Override
    public void start(Stage window) {
        Debug.set(true);
        Debug.trace("ATM starting...");
        Debug.trace("Main::start");

        Bank bank = new Bank();
        bank.addBankAccount(10001, 11111, 100);
        bank.addBankAccount(10002, 22222, 50);

        Main.mainFrame = new MainFrame();
        Model model = new Model(bank);
        View view = new View();
        Controller controller = new Controller();

        model.setView(view);
        model.setController(controller);

        controller.setModel(model);
        controller.setView(view);

        view.setModel(model);
        view.setController(controller);

        mainFrame.showScreen(Screen.getScreen(Login.class));
        // view.start(window);
        // model.initialise("Welcome to the ATM");
        // model.display();

        Debug.trace("ATM running...");
    }

}
