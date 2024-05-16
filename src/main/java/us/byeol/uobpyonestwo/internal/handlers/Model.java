package us.byeol.uobpyonestwo.internal.handlers;

import us.byeol.uobpyonestwo.internal.Bank;
import us.byeol.uobpyonestwo.misc.Debug;

/**
 * Class to handle interactions with the ATM.
 */
public class Model {

    private static final String ACCOUNT_NO = "account_no";
    private static final String PASSWORD = "password";
    private static final String LOGGED_IN = "logged_in";
    private static final String ATM_TITLE = "Bank ATM";

    /**
     * Gets the ATM title.
     */
    public static String getTitle() {
        return Model.ATM_TITLE;
    }

    private final Bank bank;

    private String state = ACCOUNT_NO;
    private int number = 0;
    private int accountNumber = -1;
    private int accountPassword = -1;
    private String display1 = null;
    private String display2 = null;

    private View view;
    private Controller controller;

    /**
     * Creates a new model for a given bank.
     *
     * @param bank the bank to create a model for.
     */
    public Model(Bank bank) {
        Debug.trace("Model::<constructor>");
        this.bank = bank;
    }

    /**
     * Initialises the ATM after a restart or log out.
     *
     * @param message the message to display.
     */
    public void initialise(String message) {
        this.setState(ACCOUNT_NO);
        this.number = 0;
        this.display1 = message;
        this.display2 =  "Enter your account number\n" +
                "Followed by \"Ent\"";
    }

    /**
     * Changes the state to a given state.
     *
     * @param newState the new state of the ATM.
     */
    public void setState(String newState) {
        if (!state.equals(newState)) {
            String oldState = this.state;
            this.state = newState;
            Debug.trace("Model::setState::changed state from "+ oldState + " to " + newState);
        }
    }

    /**
     * Processes a number inputted to the ATM.
     */
    public void processNumber(String label) {
        this.number = this.number * 10 + label.charAt(0) - '0';
        this.display1 = "" + number;
        this.display();
    }

    /**
     * Clears the ATM's display to be blank.
     */
    public void processClear() {
        this.number = 0;
        this.display1 = "";
        this.display();
    }

    /**
     * Processes the ATM being told it has all the input it needs.
     */
    public void processEnter() {
        switch (state) {
            case ACCOUNT_NO:
                this.accountNumber = this.number;
                this.number = 0;
                this.setState(PASSWORD);
                this.display1 = "";
                this.display2 = "Now enter your password\n" +
                        "Followed by \"Ent\"";
                break;
            case PASSWORD:
                this.accountPassword = number;
                this.number = 0;
                this.display1 = "";
                if (bank.login(accountNumber, accountPassword)) {
                    this.setState(LOGGED_IN);
                    this.display2 = "Accepted\n" +
                            "Now enter the transaction you require";
                } else
                    this.initialise("Unknown account/password");
        }
        this.display();
    }

    /**
     * Attempts to withdraw the amount specified and displays the result on the ATM.
     */
    public void processWithdraw() {
        if (state.equals(LOGGED_IN) ) {
            this.display2 = bank.withdraw(number) ? "Withdrawn: " + number : "You do not have sufficient funds.";
            this.number = 0;
            this.display1 = "";
        } else
            this.initialise("You are not logged in");
        this.display();
    }

    /**
     * Attempts to withdraw the amount specified and displays the result on the ATM, along with the balance.
     */
    public void processWithdrawBal() {
        if (state.equals(LOGGED_IN) ) {
            this.display2 = (bank.withdraw(number) ? "Withdrawn: " + number : "You do not have sufficient funds.") +
                    "Your balance is "+ bank.getBalance() + ".";
            this.number = 0;
            this.display1 = "";
        } else
            this.initialise("You are not logged in");
        this.display();
    }

    /**
     * Attempts to deposit the amount specified and displays the result on the ATM.
     */
    public void processDeposit() {
        if (state.equals(LOGGED_IN)) {
            this.display1 = "";
            this.display2 = bank.deposit(number) ? "Deposited: " + number : "You cannot deposit this amount.";
            this.number = 0;
        } else
            this.initialise("You are not logged in");
        this.display();
    }

    /**
     * Attempts to show the balance to the user if logged in.
     */
    public void processBalance() {
        if (state.equals(LOGGED_IN)) {
            this.number = 0;
            this.display2 = "Your balance is: " + bank.getBalance();
        } else
            this.initialise("You are not logged in");
        this.display();
    }

    /**
     * Attempts to change the overdraft limit of the logged in account.
     */
    public void processOverdraftChange() {
        if (state.equals(LOGGED_IN)) {
            this.display2 = bank.setOverdraft(number) ? "Your new overdraft limit is: " + number : "Your overdraft limit cannot be set to this amount.";
            this.number = 0;
        } else
            this.initialise("You are not logged in");
        this.display();
    }

    /**
     * Attempts to show the history of the account if logged in.
     */
    public void processAccountHistory() {
        if (state.equals(LOGGED_IN)) {
            this.display2 = String.join("\n", bank.getHistory());
            this.number = 0;
        } else
            this.initialise("You are not logged in.");
        this.display();
    }

    /**
     * Proccesses logging out of the ATM.
     */
    public void processFinish() {
        if (state.equals(LOGGED_IN)) {
            this.setState(ACCOUNT_NO);
            this.number = 0;
            this.display2 = "Welcome: Enter your account number";
            this.bank.logout();
        } else
            this.initialise("You are not logged in");
        this.display();
    }

    /**
     * Called when an unknown key is pressed.
     *
     * @param action the action which was recieved.
     */
    public void processUnknownKey(String action) {
        Debug.trace("Model::processUnknownKey::Unknown action[" + action + "], re-initialising");
        this.initialise("Invalid command");
        this.display();
    }

    /**
     * Updates the view of this model.
     */
    public void display() {
        Debug.trace("Model::display");
        this.view.update();
    }

    /**
     * Gets the first display of the model.
     *
     * @return the first display.
     */
    public String getFirstDisplay() {
        return this.display1;
    }

    /**
     * Gets the second display of the model.
     *
     * @return the second display.
     */
    public String getSecondDisplay() {
        return this.display2;
    }

    /**
     * Gets the view of this model.
     */
    public View getView() {
        return this.view;
    }

    /**
     * Sets the view of this model.
     *
     * @param view the new view.
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Gets the controller of this model.
     */
    public Controller getController() {
        return this.controller;
    }

    /**
     * Sets the controller of this model.
     *
     * @param controller the new controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

}