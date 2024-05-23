package us.byeol.uobpyonestwo.breakout.game;

import javafx.scene.input.KeyEvent;
import us.byeol.uobpyonestwo.breakout.game.enums.State;
import us.byeol.uobpyonestwo.breakout.gui.Model;
import us.byeol.uobpyonestwo.breakout.misc.Debug;

public class Controller {

    private Model model;

    /**
     * Creates a new controller [we specify this for debugging purposes].
     */
    public Controller() {
        Debug.trace("Controller::<constructor>");
    }

    /**
     * Sets the model for this controller.
     *
     * @param model the model.
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Called when a key is interacted with.
     *
     * @param event the key event.
     */
    public void userKeyInteraction(KeyEvent event) {
        Debug.trace("Controller::userKeyInteraction: keyCode = " + event.getCode());
        switch (event.getCode()) {
            case LEFT ->
                    model.moveBat(-2);
            case RIGHT ->
                    model.moveBat(2);
            case A ->
                    model.moveSecondBat(-2);
            case D ->
                    model.moveSecondBat(2);
            case F ->
                    model.setFast(true);
            case N ->
                    model.setFast(false);
            case S ->
                    model.setState(State.FINISHED);
        }
    }

}
