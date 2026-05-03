package bank;

/**
 * I created FixedAccount to represent a fixed-term deposit account.
 *
 * <p> The idea is that the account can earn 10% interest at the end of the term,
 * but only if the customer has not made an early withdrawal.</p>
 *
 * <p><b>Assumption:</b> I simulated the end of the term by calling addInterest(), because this version
 * of the system does not track real calendar dates.</p>
 *
 * @author luizagomes
 */

public class FixedAccount extends Account {

    /* I use this flag to remember if the customer has withdrawn money early. */
    private boolean earlyWithdrawal;

     /*
     * This constructor creates a FixedAccount and starts earlyWithdrawal as false.
     *
     * That means the customer has not withdrawn money early yet, so the account can
     * still receive interest later.
     */
    /**
     * Constructs a FixedAccount with the given details.
     * The early withdrawal flag is initially false.
     *
     * @param accountNumber the unique account number
     * @param balance       the opening balance (must be >= 0)
     * @param pin           the 4-digit PIN for ATM login
     * @param holderName    the name of the account holder
     * @throws NegativeValueException if balance is negative
     */
    public FixedAccount(int accountNumber, double balance, int pin, String holderName)
            throws NegativeValueException {
        super(accountNumber, balance, pin, holderName);
        this.earlyWithdrawal = false;
    }

    /**
     * This method withdraws money from the fixed account.
     *
     * Before subtracting the money, I run the basic ATM checks. Then I set
     * earlyWithdrawal to true, because any withdrawal means the customer loses the
     * fixed-term interest in this system.
     *
     * @param amount the amount to withdraw
     * @throws NegativeValueException      if the amount is invalid
     * @throws InsuficientBalanceException if balance is insufficient
     */
    @Override
    public void withdraw(double amount)
            throws NegativeValueException, InsuficientBalanceException {
        basicWithdrawChecks(amount);
        earlyWithdrawal = true;
        balance -= amount;
    }

    /*
     * This method adds 10% interest only when no early withdrawal has happened.
     *
     * If earlyWithdrawal is true, I leave the balance unchanged because the customer
     * has already broken the fixed-term condition.
     */
    @Override
    public void addInterest() {
        if (!earlyWithdrawal) {
            double interest = balance * 0.10;
            balance += interest;
        }
        // If there was an early withdrawal, I do not add any interest.
    }

        /* This method lets the program check whether an early withdrawal has already happened. */

    /**
     * Returns whether an early withdrawal has been made on this account.
     *
     * @return true if an early withdrawal occurred, false otherwise
     */
    public boolean hasEarlyWithdrawal() {
        return earlyWithdrawal;
    }

        /* This returns the account type name that will be shown in the ATM screen and tests. */
    /**
     * Returns the account type label.
     *
     * @return "Fixed"
     */
    @Override
    public String getAccountType() {
        return "Fixed";
    }
}
