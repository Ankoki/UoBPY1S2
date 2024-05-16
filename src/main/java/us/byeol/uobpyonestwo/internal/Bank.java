package us.byeol.uobpyonestwo.internal;

import us.byeol.uobpyonestwo.misc.Debug;

import java.util.List;
import java.util.ArrayList;

/**
 * Class to handle transactions and account storage.
 */
public class Bank {

    private final int maxAccounts = 10;
    private final BankAccount[] accounts = new BankAccount[maxAccounts];

    private int numAccounts = 0;
    private BankAccount account = null;

    /**
     * Creates a new Bank object.
     */
    public Bank() {
        Debug.trace( "Bank::<constructor>");
    }

    /**
     * Creates a new bank account with the given information.
     *
     * @param accountNumber the number to be associated with the account.
     * @param accountPassword the password of the account.
     * @param initialBalance the initial balance of the account.
     *
     * @return a new BankAccount.
     */
    public BankAccount makeBankAccount(int accountNumber, int accountPassword, int initialBalance) {
        return new BankAccount(accountNumber, accountPassword, initialBalance);
    }

    /**
     * Adds a new bank account into the bank's system.
     *
     * @param account the bank account to add.
     *
     * @return true if successfully added, false if the bank cannot take any more accounts.
     */
    public boolean addBankAccount(BankAccount account) {
        if (numAccounts < maxAccounts) {
            this.accounts[numAccounts] = account;
            this.numAccounts++ ;
            Debug.trace("Bank::addBankAccount::New bank account added[" + account + "]");
            return true;
        } else {
            Debug.trace("Bank::addBankAccount::Can't add bank account - too many accounts");
            return false;
        }
    }

    /**
     * Adds a new bank account to the bank's system.
     *
     * @param accountNumber the number to be associated with the account.
     * @param accountPassword the password of the account.
     * @param initialBalance the initial balance of the account.
     *
     * @return true if successfully added, false if the bank cannot take anymore accounts.
     */
    public boolean addBankAccount(int accountNumber, int accountPassword, int initialBalance) {
        return this.addBankAccount(this.makeBankAccount(accountNumber, accountPassword, initialBalance));
    }

    /**
     * Logs into the account with the given number and password.
     *
     * @param accountNumber the account number to look for.
     * @param accountPassword the password for the account.
     *
     * @return true if successful, false if no such combination was found.
     */
    public boolean login(int accountNumber, int accountPassword) {
        Debug.trace("Bank::login::accountNumber[" + accountNumber + "]");
        Debug.trace("Bank::login::accountPassword[" + accountPassword + "]");
        this.logout();
        for (BankAccount account : this.accounts) {
            if (account == null) continue; // Not sure why there is a null entry, however this will be a fail-safe.
            if (account.getAccountNumber() == accountNumber && account.getAccountPassword() == accountPassword) {
                this.account = account;
                Debug.trace("Bank::login::Successfully logged in");
                return true;
            }
        }
        Debug.trace("Bank::login::No account found with the given information");
        return false;
    }

    /**
     * Logs out of the account if one is logged in.
     */
    public void logout() {
        if (this.loggedIn()) {
            Debug.trace("Bank::logout::Logging out accountNumber[" + this.account.getAccountNumber() + "]");
            this.account = null;
        } else
            Debug.trace("Bank::logout::Not logged in");
    }

    /**
     * Checks if there is an account logged in.
     *
     * @return true if logged in, else false.
     */
    public boolean loggedIn() {
        return this.account != null;
    }

    /**
     * Deposit money into the current account.
     *
     * @param amount the amount to deposit.
     *
     * @return true if successful, else false.
     */
    public boolean deposit(int amount) {
        if (this.loggedIn())
            return this.account.deposit(amount);
        return false;
    }

    /**
     * Withdraws money from the current account.
     *
     * @param amount to withdraw.
     *
     * @return true if successful, else false.
     */
    public boolean withdraw(int amount) {
        if (this.loggedIn())
            return this.account.withdraw(amount);
        return false;
    }

    /**
     * Sets the overdraft of the current account.
     *
     * @param overdraft thew new overdraft limit.
     *
     * @return true if successful, else false.
     */
    public boolean setOverdraft(int overdraft) {
        if (this.loggedIn())
            return this.account.setOverdraft(overdraft);
        return false;
    }

    /**
     * Gets the balance of the current account.
     *
     * @return the balance of the account, or if not logged in, -1.
     */
    public int getBalance() {
        if (this.loggedIn())
            return account.getBalance();
        return -1;
    }

    /**
     * Gets the history of the current account.
     *
     * @return the history of the account, or an empty list.
     */
    public List<String> getHistory() {
        if (this.loggedIn())
            return account.getHistory();
        return new ArrayList<>();
    }

}
