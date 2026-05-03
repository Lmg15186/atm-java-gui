package bank;

/**
 * I created NegativeValueException to handle invalid money values, such as zero
 * or negative deposits, withdrawals or limits.
 *
 * <p>Using my own exception makes the error clearer and easier to display in the
 * ATM interface.</p>
 *
 * @author luizagomes
 */

public class NegativeValueException extends Exception {

    /* This constructor uses a default message when I do not pass a custom one. */

    public NegativeValueException() {
        super("Amount must be a positive whole number greater than zero.");
    }



    /**
     * This constructor lets me pass a more specific error message.
     *
     * @param message the detail message describing the error
     */
    public NegativeValueException(String message) {
        super(message);
    }
}
