package bank;

/**
 * I created InsuficientBalanceException to handle cases where the user tries to
 * withdraw more money than allowed.
 *
 * <p>This can happen because of the balance itself or because of a withdrawal limit.</p>
 *
 * @author luizagomes
 */
public class InsuficientBalanceException extends Exception {

    /* This constructor uses a default insuficient balance message. */

    public InsuficientBalanceException() {
        super("Insuficient balance. Please enter a lower amount.");
    }

        /* This constructor lets me show a more specific insufficient balance message. */

    /**
     * Constructs an InsuficientBalanceException with a custom message.
     *
     * @param message the detail message describing the error
     */
    public InsuficientBalanceException(String message) {
        super(message);
    }
}
