package bank;

/**
 * I created SavingsAccount to represent a regular savings account.
 *
 * <p>This account earns 2% interest and also has a daily withdrawal limit. In this
 * version, the limit is set manually and does not reset automatically by date.</p>
 *
 *
 * @author luizagomes
 */


public class SavingsAccount extends Account {

    /* This stores the maximum amount the user can withdraw from this savings account. */
    private double dailyWithdrawalLimit;

    
      /*
     * This constructor creates a SavingsAccount and also sets the daily withdrawal limit.
     *
     * I check the limit before saving it because a zero or negative limit would make
     * the account rules invalid.
     */
    /**
     * Constructs a SavingsAccount with the given details.
     *
     * @param accountNumber       the unique account number
     * @param balance             the opening balance (must be >= 0)
     * @param pin                 the 4-digit PIN for ATM login
     * @param holderName          the name of the account holder
     * @param dailyWithdrawalLimit the initial daily withdrawal limit (must be > 0)
     * @throws NegativeValueException if balance or dailyWithdrawalLimit is invalid
     */
    public SavingsAccount(int accountNumber, double balance, int pin,
            String holderName, double dailyWithdrawalLimit) throws NegativeValueException {
        super(accountNumber, balance, pin, holderName);
        if (dailyWithdrawalLimit <= 0) {
            throw new NegativeValueException("Daily withdrawal limit must be greater than zero.");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    
     /*
     * This method lets the daily withdrawal limit be changed.
     *
     * I added validation again because the new limit still needs to be greater than
     * zero.
     */
    /**
     * Sets a new daily withdrawal limit for this account.
     *
     * @param dailyWithdrawalLimit the new limit (must be > 0)
     * @throws NegativeValueException if the provided limit is zero or negative
     */
    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit)
            throws NegativeValueException {
        if (dailyWithdrawalLimit <= 0) {
            throw new NegativeValueException("Daily withdrawal limit must be greater than zero.");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    
        /* This getter returns the current daily withdrawal limit for this account. */

    /**
     * Returns the current daily withdrawal limit.
     *
     * @return the daily withdrawal limit
     */
    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    
        /*
     * This method withdraws money from the savings account.
     *
     * I check the daily limit first because SavingsAccount has this extra rule.
     * After that, I use the basic ATM checks and subtract the amount from the balance.
     */
    /**
     * Withdraws from the savings account, respecting the daily withdrawal limit.
     *
     * @param amount the amount to withdraw
     * @throws NegativeValueException      if the amount is invalid
     * @throws InsuficientBalanceException if balance is insufficient
     */
    @Override
    public void withdraw(double amount)
            throws NegativeValueException, InsuficientBalanceException {
        if (amount > dailyWithdrawalLimit) {
            throw new InsuficientBalanceException(
                String.format("Daily withdrawal limit of $%.2f exceeded.", dailyWithdrawalLimit));
        }
        basicWithdrawChecks(amount);
        balance -= amount;
    }

    /*
     * This method adds 2% interest to the savings account balance.
     *
     * I calculate the interest first, then add it to the balance.
     */
    @Override
    public void addInterest() {
        double interest = balance * 0.02;
        balance += interest;
    }

    
        /* This returns the account type name that will be shown in the ATM screen and tests. */

    /**
     * Returns the account type label.
     *
     * @return "Savings"
     */
    @Override
    public String getAccountType() {
        return "Savings";
    }
}
