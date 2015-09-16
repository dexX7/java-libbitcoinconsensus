package co.bitwatch.libbitcoinconsensus.exceptions;

/**
 * Exception for mismatch of transaction size
 */
public class TxSizeMismatchException extends IllegalArgumentException {

    public TxSizeMismatchException() {
        super();
    }

    public TxSizeMismatchException(String message) {
        super(message);
    }

}
