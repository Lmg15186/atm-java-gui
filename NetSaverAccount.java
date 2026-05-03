package bank;

/**
 * I created NetSaverAccount to represent a high-interest online savings account.
 *
 *
 * <p>This account earns 5% interest and has a fixed daily withdrawal limit of
 * $1,000. The customer cannot change this limit because it is set by the bank.</p>
 *
 *
 * @author luizagomes
 */

public class NetSaverAccount extends Account {

    /* This is the fixed daily withdrawal limit for every NetSaver account. */
    private static final double DAILY_WITHDRAWAL_LIMIT = 1000.00;

     /*
     * This constructor creates a NetSaverAccount.
     *
     * I call the parent constructor because the basic account setup is already done
     * in Account.
     */
    /**
     * Constructs a NetSaverAccount with the given details.
     *
     * @param accountNumber the unique account number
     * @param balance       the opening balance (must be >= 0)
     * @param pin           the 4-digit PIN for ATM login
     * @param holderName    the name of the account holder
     * @throws NegativeValueException if balance is negative
     */
    public NetSaverAccount(int accountNumber, double balance, int pin, String holderName)
            throws NegativeValueException {
        super(accountNumber, balance, pin, holderName);
    }

        /* This getter returns the fixed daily limit so it can be checked or displayed. */

    /**
     * Returns the fixed daily withdrawal limit for this account type.
     *
     * @return the daily withdrawal limit ($1,000)
     */
    public double getDailyWithdrawalLimit() {
        return DAILY_WITHDRAWAL_LIMIT;
    }

    
       /*
     * This method handles withdrawals for NetSaver accounts.
     *
     * I check the $1,000 daily limit first because this account has that extra rule.
     * If the amount is within the limit, I then run the normal ATM checks and
     * subtract the money from the balance.
     */
    /**
     * Withdraws from the NetSaver account, enforcing the fixed $1,000 daily limit.
     *
     * @param amount the amount to withdraw
     * @throws NegativeValueException      if the amount is invalid
     * @throws InsuficientBalanceException if balance is insufficient or daily limit exceeded
     */
    @Override
    public void withdraw(double amount)
            throws NegativeValueException, InsuficientBalanceException {
        if (amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new InsuficientBalanceException(
                String.format("Daily withdrawal limit of $%.2f exceeded for NetSaver Account.",
                    DAILY_WITHDRAWAL_LIMIT));
        }
        basicWithdrawChecks(amount);
        balance -= amount;
    }

    /*
     * This method adds 5% interest to the current balance.
     *
     * I calculate the interest separately first so the logic is easier to read.
     */
    @Override
    public void addInterest() {
        double interest = balance * 0.05;
        balance += interest;
    }

        /* This returns the account type name that will be shown in the ATM screen and tests. */

    /**
     * Returns the account type label.
     *
     * @return "NetSaver"
     */
    @Override
    public String getAccountType() {
        return "NetSaver";
    }
}
