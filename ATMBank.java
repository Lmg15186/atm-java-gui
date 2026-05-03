package bank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The ATMBank class is responsible for storing and managing the accounts used 
 * in the ATM system.
 *
 * <p> I created this class to act as a kind of simple ‘database’ 
 * within the programme, where accounts are stored in memory whilst the system 
 * is running.It also has methods to search for an account by number and verify 
 * that the PIN entered by the user is correct before granting access.</p>
 *
 * <p><b>Assumptions:</b></p>
 * <ul>
 *   <li>Accounts are already created when the programme starts.</li>
 *   <li> I haven’t used a database or external file, so the information is loaded.
 * directly into the code whilst the system is running.</li>
 *   <li> Account numbers and PINs are fixed. This makes it easier to test the 
 * system, because I already know which data to use to log in.</li>
 *   <li> I skipped account number 1004 on purpose.
 * This was a design choice because, in Japanese culture, the number 4 is sometimes
 * considered unlucky, as "shi" can sound like the word for death.
 * Because of this, I decided not to assign account 1004 to any customer. </li>
 * </ul>
 *
 * <p><b>Test Login Credentials:</b></p>
 * <pre>
 *   Account 1001 / PIN 1235  - Savings Account  (Roshan,    $2,000.00, limit $500)
 *   Account 1002 / PIN 2222  - NetSaver Account (Ana Clara, $3,500.00)
 *   Account 1003 / PIN 3333  - Cheque Account   (Luiza,     $1,200.00)
 *   Account 1005 / PIN 5555  - Fixed Account    (Willian,   $5,000.00)
 * </pre>
 * 
 *  @author luizagomes
 */

public class ATMBank {

    /** This Map stores all the accounts in the system.
     * I used the account number as the key, so it is easier to find the correct
     * Account object when the user enters their account number. */
    private Map<Integer, Account> accounts;

    /**
     *  This constructor creates the ATMBank object and loads the test accounts
     * when the program starts.
     *
     * I added the accounts directly in the code because this version of the ATM
     * does not use a database or external file.
     *
     * If there is a problem while creating one of these accounts, the program throws
     * a RuntimeException. In this case, the error would probably come from the fixed
     * values I wrote in the code, not from user input.
     *
     * <p>Note: Account number 1004 is skipped on purpose as a design choice, because the
     * number 4 can be considered unlucky in Japanese culture.</p>
     */
    
    public ATMBank() {
        accounts = new HashMap<>();
        try {
    /*
     * Here I am adding the test accounts to the system.
     * I used a HashMap because each account can be stored using its account number,
     * which makes it easier to find the right account later during login.
     */
            // Savings Account: Account 1001 / PIN 1235
            accounts.put(1001, new SavingsAccount(1001, 2000.00, 1235, "Roshan", 500.00));

            // NetSaver Account: Account 1002 / PIN 2222
            accounts.put(1002, new NetSaverAccount(1002, 3500.00, 2222, "Ana Clara"));

            // Cheque Account: Account 1003 / PIN 3333
            accounts.put(1003, new ChequeAccount(1003, 1200.00, 3333, "Luiza"));

            /**  I skipped account number 1004 on purpose.
        * This was a design choice because, in Japanese culture, the number 4 is sometimes
        * considered unlucky, as "shi" can sound like the word for death.
        *
        * Because of this, I decided not to assign account 1004 to any customer.
        */
            
            // Fixed Account used for testing: Account 1005 / PIN 5555
            accounts.put(1005, new FixedAccount(1005, 5000.00, 5555, "Willian"));

            /*
     * After loading the accounts, I made the map unmodifiable.
     * I did this to avoid changing the test accounts by accident after the bank
     * has been initialised.
            */
            
            accounts = Collections.unmodifiableMap(accounts);

        } catch (NegativeValueException e) {
            /*
     * This error should not normally happen because the account values are already
     * written directly in the code and they are valid.
     *
     * If this exception happens, it probably means there is a mistake in one of the
     * hardcoded account values, so I throw a RuntimeException to stop the program.
     */
            throw new RuntimeException("Failed to initialise bank accounts: " + e.getMessage());
        }
    }

    /*
 * This method searches for an account using the account number entered.
 *
 * I created this method to keep the search logic in one place, instead of
 * writing accounts.get(accountNumber) every time I need to find an account.
 *
 * If the account exists, it returns the Account object.
 * If the account number does not exist in the map, it returns null.
 */
    
    /**
     * Looks up an account by its account number.
     *
     * @param accountNumber the account number to search for
     * @return the matching Account, or null if not found
     */
    public Account findAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    /*
 * This method is used when the user tries to log in to the ATM system.
 *
 * First, it tries to find the account using the account number.
 * Then, if the account exists, it checks whether the PIN entered by the user
 * matches the PIN stored in that account.
 *
 * If both the account number and PIN are correct, the method returns the account
 * and the user can continue using the ATM.
 *
 * If the account does not exist or the PIN is wrong, it returns null.
 */
    /**
     * Attempts to log in with the given account number and PIN.
     *
     * @param accountNumber the account number entered by the user
     * @param pin           the PIN entered by the user
     * @return the authenticated Account if credentials are correct, or null otherwise
     */
    public Account login(int accountNumber, int pin) {
        Account account = findAccount(accountNumber);
        if (account != null && account.validatePin(pin)) {
            return account;
        }
        return null;
    }
}
