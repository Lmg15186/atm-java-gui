package bank;

/**
 * I created ChequeAccount to represent a normal transaction account.
 *
 *
 * <p>This account does not earn interest and it does not have an extra daily limit.
 * It still uses the basic ATM rules from Account, such as valid note amounts
 * and sufficient balance.</p>
 *
 * @author luizagomes
 */
public class ChequeAccount extends Account {

    /**
     * This constructor creates a ChequeAccount.
     *
     * I call super(...) because the common account details are already handled in
     * the parent Account class.
     *
     * @param accountNumber the unique account number
     * @param balance       the opening balance (must be >= 0)
     * @param pin           the 4-digit PIN for ATM login
     * @param holderName    the name of the account holder
     * @throws NegativeValueException if balance is negative
     */
    public ChequeAccount(int accountNumber, double balance, int pin, String holderName)
            throws NegativeValueException {
        super(accountNumber, balance, pin, holderName);
    }

    /**
     * This withdraw method uses only the standard withdrawal checks.
     *
     * Cheque accounts do not have an extra daily withdrawal limit, so after the
     * basic validation passes, I subtract the amount from the balance.
     * 
     * @param amount the amount to withdraw
     * @throws NegativeValueException      if the amount is invalid
     * @throws InsuficientBalanceException if balance is insufficient
     */
    @Override
    public void withdraw(double amount)
            throws NegativeValueException, InsuficientBalanceException {
        basicWithdrawChecks(amount);
        balance -= amount;
    }

 /*
     * Cheque accounts do not earn interest in this system.
     *
     * I still had to include this method because Account requires every child class
     * to implement addInterest().
     */
    @Override
    public void addInterest() {
         // I leave this empty because cheque accounts do not receive interest.
    }

        /* This returns the account type name that will be shown in the ATM screen and tests. */
    /**
     * Returns the account type label.
     *
     * @return "Cheque"
     */
    @Override
    public String getAccountType() {
        return "Cheque";
    }
}
