package bank;

/*
 * I created TestATM to test the business logic before relying on the GUI.
 *
 * This class runs console tests for each account type, including successful
 * operations and expected errors. This helps me prove that the account classes
 * work before the user interacts with the ATM screen.
 */
public class TestATM {

    /*
     * This main method runs all the test sections one by one.
     *
     * I separated the tests into smaller methods so the output is easier to read
     * and the testing structure is more organised.
     */
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   AITBank ATM - Full System Test");
        System.out.println("===========================================\n");

        testSavingsAccount();
        testNetSaverAccount();
        testChequeAccount();
        testFixedAccount();
        testExceptionHandling();
        testLoginSystem();

        System.out.println("\n===========================================");
        System.out.println("   All tests completed.");
        System.out.println("===========================================");
    }
    // Account type tests section.

    /*
     * This test checks the SavingsAccount behaviour.
     *
     * I test deposit, a valid withdrawal, an over-limit withdrawal, and then the
     * 2% interest calculation.
     */
    private static void testSavingsAccount() {
        System.out.println("--- Savings Account Tests ---");
        try {
            SavingsAccount savings = new SavingsAccount(1001, 1000.00, 1235, "Roshan", 300.00);

            savings.deposit(200.00);
            System.out.printf("  Deposited $200.00. Balance: $%.2f%n", savings.getBalance());

            savings.withdraw(200.00);
            System.out.printf("  Withdrew  $200.00. Balance: $%.2f%n", savings.getBalance());

            // I am testing what happens when the user goes over the daily limit.
            try {
                savings.withdraw(400.00);
            } catch (InsuficientBalanceException e) {
                System.out.println("  Expected error (daily limit): " + e.getMessage());
            }

            savings.addInterest();
            System.out.printf("  After interest (2%%): $%.2f%n", savings.getBalance());

        } catch (NegativeValueException | InsuficientBalanceException e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
        System.out.println();
    }

    /*
     * This test checks the NetSaverAccount behaviour.
     *
     * I test a normal withdrawal, a withdrawal above the $1,000 limit, and then the
     * 5% interest calculation.
     */
    private static void testNetSaverAccount() {
        System.out.println("--- NetSaver Account Tests ---");
        try {
            NetSaverAccount netSaver = new NetSaverAccount(1002, 1500.00, 2222, "Ana Clara");

            netSaver.withdraw(500.00);
            System.out.printf("  Withdrew  $500.00. Balance: $%.2f%n", netSaver.getBalance());

            // I am testing what happens when the user goes over the $1,000 NetSaver limit.
            try {
                netSaver.withdraw(1200.00);
            } catch (InsuficientBalanceException e) {
                System.out.println("  Expected error (daily limit): " + e.getMessage());
            }

            netSaver.addInterest();
            System.out.printf("  After interest (5%%): $%.2f%n", netSaver.getBalance());

        } catch (NegativeValueException | InsuficientBalanceException e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
        System.out.println();
    }

    /*
     * This test checks the ChequeAccount behaviour.
     *
     * I test a valid ATM amount, an invalid note combination, and confirm that
     * addInterest() does not change the balance.
     */
    private static void testChequeAccount() {
        System.out.println("--- Cheque Account Tests ---");
        try {
            ChequeAccount cheque = new ChequeAccount(1003, 2000.00, 3333, "Luiza");

            cheque.withdraw(170.00); // valid: 100 + 50 + 20
            System.out.printf("  Withdrew  $170.00. Balance: $%.2f%n", cheque.getBalance());

            // I am testing an amount that the ATM cannot pay with its available notes.
            try {
                cheque.withdraw(30.00); // cannot make $30 with $20, $50, $100 notes
            } catch (NegativeValueException e) {
                System.out.println("  Expected error (invalid notes): " + e.getMessage());
            }

            cheque.addInterest(); // no interest for cheque accounts
            System.out.printf("  After addInterest (no change): $%.2f%n", cheque.getBalance());

        } catch (NegativeValueException | InsuficientBalanceException e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
        System.out.println();
    }

    /*
     * This test checks the FixedAccount rules.
     *
     * I test one account that receives interest because there was no withdrawal,
     * and another account that loses the interest after an early withdrawal.
     */
    private static void testFixedAccount() {
        System.out.println("--- Fixed Account Tests ---");
        try {
            // First I test the case where there is no early withdrawal, so interest should be added.
            FixedAccount fixedA = new FixedAccount(1005, 3000.00, 5555, "Willian");
            fixedA.addInterest();
            System.out.printf("  No withdrawal → interest added: $%.2f%n", fixedA.getBalance());

            // Then I test the case where an early withdrawal happens, so interest should not be added.
            FixedAccount fixedB = new FixedAccount(1005, 3000.00, 5555, "Willian");
            fixedB.withdraw(200.00);
            System.out.printf("  After early withdrawal: $%.2f%n", fixedB.getBalance());
            fixedB.addInterest();
            System.out.printf("  After addInterest (no interest due): $%.2f%n", fixedB.getBalance());

        } catch (NegativeValueException | InsuficientBalanceException e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
        System.out.println();
    }

    /*
     * This test checks whether my custom exceptions are being triggered correctly.
     *
     * I used invalid examples on purpose, such as negative balances, negative
     * deposits, insufficient balance and invalid daily limits.
     */
    private static void testExceptionHandling() {
        System.out.println("--- Exception Handling Tests ---");

        // I am testing an account created with an invalid negative opening balance.
        try {
            new ChequeAccount(9001, -500.00, 1111, "Bad Account");
        } catch (NegativeValueException e) {
            System.out.println("  NegativeValueException (negative balance): " + e.getMessage());
        }

        // I am testing a deposit with an invalid negative amount.
        try {
            ChequeAccount c = new ChequeAccount(9002, 500.00, 1111, "Test User");
            c.deposit(-100.00);
        } catch (NegativeValueException e) {
            System.out.println("  NegativeValueException (negative deposit): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected: " + e.getMessage());
        }

        // I am testing a withdrawal that is higher than the available balance.
        try {
            ChequeAccount c = new ChequeAccount(9003, 100.00, 1111, "Test User");
            c.withdraw(200.00);
        } catch (InsuficientBalanceException e) {
            System.out.println("  InsuficientBalanceException: " + e.getMessage());
        } catch (NegativeValueException e) {
            System.out.println("  Unexpected: " + e.getMessage());
        }

        // I am testing a SavingsAccount with an invalid negative daily limit.
        try {
            new SavingsAccount(9004, 500.00, 1111, "Test User", -100.00);
        } catch (NegativeValueException e) {
            System.out.println("  NegativeValueException (neg limit): " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * This test checks the login system.
     *
     * I test a correct login, a wrong PIN and an unknown account number to make
     * sure the bank returns the correct result in each case.
     */
    private static void testLoginSystem() {
        System.out.println("--- Login System Tests ---");
        ATMBank bank = new ATMBank();

        // I am testing a login with the correct account number and PIN.
        Account acc = bank.login(1001, 1235);
        if (acc != null) {
            System.out.println("  Login 1001/1235 → OK: " + acc.getHolderName()
                + " (" + acc.getAccountType() + ")");
        }

        // I am testing the correct account number but the wrong PIN.
        Account wrong = bank.login(1001, 9999);
        System.out.println("  Login 1001/9999 → " + (wrong == null ? "REJECTED (correct)" : "ERROR"));

        // I am testing an account number that does not exist.
        Account unknown = bank.login(9999, 1234);
        System.out.println("  Login 9999/1234 → " + (unknown == null ? "NOT FOUND (correct)" : "ERROR"));

        System.out.println();
    }
}

