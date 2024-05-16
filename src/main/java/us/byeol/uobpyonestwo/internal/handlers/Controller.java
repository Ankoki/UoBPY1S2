package us.byeol.uobpyonestwo.internal.handlers;

import us.byeol.uobpyonestwo.misc.Debug;

import java.util.Arrays;

/**
 * Class to handle all the processes of a view.
 */
public class Controller {

    private Model model;
    private View view;

    /**
     * Creates a new Controller.
     */
    public Controller() {
        Debug.trace("Controller::<constructor>");
    }

    /**
     * Processes the given action for the model.
     */
    public void process(String action) {
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

    /**
     * Sets the model of this controller.
     *
     * @param model the new model.
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Sets the view of this controller.
     *
     * @param view the new view.
     */
    public void setView(View view) {
        this.view = view;
    }

}