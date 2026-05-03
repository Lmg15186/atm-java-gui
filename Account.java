package bank;

/**
 * I created Account as an abstract base class because all account types share
 * the same basic information, such as account number, balance, PIN and holder name.
 *
 * <p> This class also keeps the common deposit and withdrawal validation rules in
 * one place, so the other account classes do not need to repeat the same code.</p>
 *
 * <p><b>Assumption:</b> For withdrawals, I assumed the ATM only gives whole-dollar amounts using
 * $20, $50 and $100 notes. Deposits can still be any positive amount.</p>
 *
 * @author luizagomes
 */

public abstract class Account {

    /** I store the account number here because it is the main ID used to find the account. */
    protected int accountNumber;

    /** This stores the current money available in the account. */
    protected double balance;

    /** I keep the PIN private because it should only be checked through validatePin(). */
    private int pin;

    /** This stores the customer's name so it can be shown on the ATM screen. */
    protected String holderName;

    /**
     * This constructor sets up the basic details that every account needs.
     *
     * <p> I added a check for negative opening balances because an account should not
     * start with an invalid balance. If the value is negative, I throw my custom
     * NegativeValueException instead of letting the object be created incorrectly. </p>
     *
     * @param accountNumber the unique account number
     * @param balance       the opening balance (must be >= 0)
     * @param pin           the 4-digit PIN for ATM login
     * @param holderName    the name of the account holder
     * @throws NegativeValueException if balance is negative
     */
    public Account(int accountNumber, double balance, int pin, String holderName)
            throws NegativeValueException {
        if (balance < 0) {
            throw new NegativeValueException("Opening balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.pin = pin;
        this.holderName = holderName;
    }

    /**
     * This method adds money to the account.
     *
     * I check if the amount is greater than zero first, because depositing zero or
     * a negative value would not make sense in this ATM system.
     *
     * @param amount the amount to deposit (must be > 0)
     * @throws NegativeValueException if the amount is zero or negative
     */
    public void deposit(double amount) throws NegativeValueException {
        if (amount <= 0) {
            throw new NegativeValueException("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    /**
     * This method checks if the ATM can pay the requested amount using only
     * $20, $50 and $100 notes.
     *
     * I used loops to try different note combinations. If one combination adds up
     * to the requested amount, the method returns true.
     * 
     * @param amount the amount to check (in whole dollars)
     * @return true if the amount can be dispensed, false otherwise
     */
    protected boolean canDispenseAmount(int amount) {
        for (int hundreds = 0; hundreds <= amount / 100; hundreds++) {
            for (int fifties = 0; fifties <= amount / 50; fifties++) {
                for (int twenties = 0; twenties <= amount / 20; twenties++) {
                    if ((hundreds * 100) + (fifties * 50) + (twenties * 20) == amount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Performs basic validation checks shared by all account types before
     * allowing a withdrawal.
     *
     * <p>Checks performed:</p>
     * <ul>
     *   <li>Amount must be a positive whole number</li>
     *   <li>Amount must be dispensable using $20, $50, $100 notes</li>
     *   <li>Account must have sufficient balance</li>
     * </ul>
     * 
     * 
     * This method keeps the basic withdrawal checks that all account types need.
     *
     * I put these checks in the parent class so Savings, NetSaver, Cheque and Fixed
     * accounts can reuse the same rules before subtracting money from the balance.
     *
     * The method checks three things: the amount must be positive, it must be a
     * whole-dollar amount that the ATM can dispense, and the account must have
     * enough balance.
     *
     * @param amount the withdrawal amount to validate
     * @throws NegativeValueException      if amount is not a positive whole number
     *                                     or cannot be dispensed by the ATM
     * @throws InsuficientBalanceException if the balance is insufficient
     */
    protected void basicWithdrawChecks(double amount)
            throws NegativeValueException, InsuficientBalanceException {

        if (amount <= 0 || amount != Math.floor(amount)) {
            throw new NegativeValueException(
                "Withdrawal amount must be a positive whole dollar amount.");
        }

        int intAmount = (int) amount;
        if (!canDispenseAmount(intAmount)) {
            throw new NegativeValueException(
                "Invalid ATM amount. Use combinations of $20, $50, and $100 only.");
        }

        if (amount > balance) {
            throw new InsuficientBalanceException(
                String.format("Insufficient balance. Available: $%.2f", balance));
        }
    }

    /*
     * This is the default withdraw method.
     *
     * It first runs the common withdrawal checks, then subtracts the amount from
     * the balance. Some child classes override this method when they need extra
     * rules, such as daily withdrawal limits.
     * 
     * @param amount the amount to withdraw
     * @throws NegativeValueException      if the amount is invalid for the ATM
     * @throws InsuficientBalanceException if the account has insufficient funds
     */
    public void withdraw(double amount)
            throws NegativeValueException, InsuficientBalanceException {
        basicWithdrawChecks(amount);
        balance -= amount;
    }

        /*I made addInterest abstract because each account type has different interest rules.
     *
     * This forces every child class to define how interest should work for that
     * specific type of account.
     *
     * Applies interest to this account. Each subclass implements this
     * according to its own interest rules.
     */
    public abstract void addInterest();


 /**
 * This method checks whether the PIN entered by the user matches the PIN stored
 * in the account.
 *
 * I keep this check inside the Account class so the GUI does not need to access
 * the PIN directly.
 *
 * @param enteredPin the PIN entered by the user
 * @return true if the PIN matches, false otherwise
 */
    public boolean validatePin(int enteredPin) {
        return this.pin == enteredPin;
    }

     /* This getter returns the account number so other classes can display or use it. 
     * Returns the account number.
     *
     * @return the account number
     */
    public int getAccountNumber() {
        return accountNumber;
    }

     /* This getter returns the current balance of the account. 
     * Returns the current balance.
     *
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }


     /* This getter returns the account holder's name.
     *
     * @return the holder's name
     */
    public String getHolderName() {
        return holderName;
    }

    /*
     * I made this abstract because each child class needs to return its own account
     * type name, for example Savings, NetSaver, Cheque or Fixed.
     *
     * @return the account type name
     */
    public abstract String getAccountType();
}
