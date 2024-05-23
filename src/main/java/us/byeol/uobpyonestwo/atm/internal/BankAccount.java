package us.byeol.uobpyonestwo.atm.internal;

import us.byeol.uobpyonestwo.atm.misc.Debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to handle storing bank account information and handle balance updates.
 */
public class BankAccount {

    private static final File DATA_FILE;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");

    static {
        DATA_FILE = new File(System.getenv("APPDATA"), "UoBPY1S2" + File.separator + "history.txt");
    }

    private final int accountNumber;
    private final int accountPassword;
    private int balance = 0;
    private int overdraft = 0;

    /**
     * Creates a new bank account with the given information.
     *
     * @param accountNumber the number associated with the account.
     * @param accountPassword the password of the account.
     */
    public BankAccount(int accountNumber, int accountPassword) {
        this(accountNumber, accountPassword, 0, 0);
    }

    /**
     * Creates a new bank account with the given information.
     *
     * @param accountNumber the number associated with the account.
     * @param accountPassword the password of the account.
     * @param initialBalance the initial balance of the account.
     */
    public BankAccount(int accountNumber, int accountPassword, int initialBalance) {
        this(accountNumber, accountPassword, initialBalance, 0);
    }

    /**
     * Creates a new bank account with the given information.
     *
     * @param accountNumber the number associated with the account.
     * @param accountPassword the password of the account.
     * @param initialBalance the initial balance of the account.
     * @param overdraft the overdraft of the account.
     */
    public BankAccount(int accountNumber, int accountPassword, int initialBalance, int overdraft) {
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
        this.balance = initialBalance;
        this.overdraft = overdraft;
    }

    /**
     * Gets the number associated with this account.
     *
     * @return the account number.
     */
    public int getAccountNumber() {
        return this.accountNumber;
    }

    /**
     * Gets the password used for this account.
     *
     * @return the password.
     */
    public int getAccountPassword() {
        return this.accountPassword;
    }

    /**
     * Withdraws a given amount from the account.
     *
     * @param amount the amount to withdraw. Should be less or equal to the balance of this account.
     * @return true if successful, else false.
     */
    public boolean withdraw(int amount) {
        Debug.trace("BankAccount::withdraw::amount[" + amount + "]");
        int leftover = (this.balance + this.overdraft) - amount;
        if (leftover < 0) {
            Debug.trace("BankAccount::withdraw::Cannot withdraw amount[overdrawn by " + leftover + "]");
            return false;
        }
        this.balance -= amount;
        Debug.trace("BankAccount::withdraw::balance[" + this.balance + "]");
        this.addHistory("Withdrew " + amount);
        return true;
    }

    /**
     * Deposits a given amount into the account.
     *
     * @param amount the amount to deposit. Should be greater than 0.
     * @return true if successful, else false.
     */
    public boolean deposit(int amount) {
        Debug.trace("BankAccount::deposit::amount[" + amount + "]");
        if (amount <= 0) {
            Debug.trace("BankAccount::deposit::Cannot deposit amount[negative or 0 given]");
            return false;
        }
        this.balance += amount;
        Debug.trace("BankAccount::deposit::Deposited amount[" + amount + "] into the account with an updated balance[" + this.balance + "]");
        this.addHistory("Deposited " + amount);
        return true;
    }

    /**
     * Gets the balance of this account.
     *
     * @return the balance.
     */
    public int getBalance() {
        Debug.trace("LocalBank::getBalance::balance[" + this.balance + "]");
        this.addHistory("Viewed balance of " + this.balance);
        return this.balance;
    }

    @Override
    public String toString() {
        return "accountNumber[" + this.accountNumber +
                "] | accountPassword[" + this.accountPassword +
                "] | balance[" + this.balance +
                "] | overdraft[" + this.overdraft + "]";
    }

    /**
     * Gets whether this account has an overdraft.
     *
     * @return true if there is an overdraft.
     */
    public boolean hasOverdraft() {
        Debug.trace("BankAccount::hasOverdraft::overdraft[" + this.overdraft + "]");
        return this.overdraft != 0;
    }

    /**
     * Sets the overdraft of this account to the value.
     *
     * @param overdraft the new overdraft limit.
     *
     * @return true if successful, else false.
     */
    public boolean setOverdraft(int overdraft) {
        if (overdraft < 0) {
            Debug.trace("BankAccount::setOverdraft::overdraft[" + overdraft + "]::account for current balance.");
            return false;
        } else if (-this.balance > 0 && -this.balance > overdraft) {
            Debug.trace("BankAccount::setOverdraft::overdraft[" + overdraft + "]::Overdraft limit too small to account for current balance.");
            return false;
        }
        this.overdraft = overdraft;
        this.addHistory("Set overdraft limit to " + overdraft);
        return true;
    }

    /**
     * Adds the given action to the accounts' history.
     *
     * @param action the action to add.
     */
    public void addHistory(String action) {
        try (FileWriter writer = new FileWriter(DATA_FILE, false)) {
            if (!DATA_FILE.exists())
                DATA_FILE.createNewFile();
            Scanner scanner = new Scanner(DATA_FILE);
            List<String> data = new ArrayList<>();
            action += " [" + DATE_FORMAT.format(new Date()) + "]";
            boolean added = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith(this.accountNumber + "|")) {
                    data.add(line);
                    continue;
                }
                line += "," + action;
                data.add(line);
                added = true;
            }
            if (!added)
                data.add(this.accountNumber + "|" + action);
            writer.write(String.join("\n", data));
        } catch (IOException ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
        }
    }

    /**
     * Gets the history of this account.
     *
     * @return the list containing all transactions and changes.
     */
    public List<String> getHistory() {
        List<String> history = new ArrayList<>();
        try {
            if (!DATA_FILE.exists()) {
                DATA_FILE.createNewFile();
                return history;
            }
            Scanner scanner = new Scanner(DATA_FILE);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith(this.accountNumber + "|"))
                    continue;
                line = line.replace(this.accountNumber + "|", "");
                Collections.addAll(history, line.split(","));
            }
        } catch (IOException ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
            return List.of("There was an error retrieving your history.");
        }
        if (history.isEmpty())
            history.add("There was no history for your account.");
        this.addHistory("Viewed account history");
        return history;
    }

}
