package us.byeol.uobpyonestwo.breakout;

import javafx.application.Application;
import javafx.stage.Stage;
import us.byeol.uobpyonestwo.breakout.game.Controller;
import us.byeol.uobpyonestwo.breakout.gui.Model;
import us.byeol.uobpyonestwo.breakout.gui.View;
import us.byeol.uobpyonestwo.breakout.misc.Debug;

public class Main extends Application {

    private static Stage primaryStage;

    /**
     * Gets the primary stage of this application.
     *
     * @return the primary stage.
     */
    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    private static final int GAME_WIDTH = 1000;
    private static final int GAME_HEIGHT = 750;

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Starts our application.
     *
     * @param window the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    public void start(Stage window) {
        Debug.trace("Main::start: Breakout starting");
        Main.primaryStage = window;
        window.setResizable(false);
        Model model = new Model(GAME_WIDTH, GAME_HEIGHT);
        View view = new View(GAME_WIDTH, GAME_HEIGHT);
        Controller controller = new Controller();
        // Links all our handlers together.
        model.setView(view);
        model.setController(controller);
        view.setModel(model);
        view.setController(controller);
        controller.setModel(model);

        view.showMenu();
        Debug.trace("Main::start: Breakout running");
    }

}
