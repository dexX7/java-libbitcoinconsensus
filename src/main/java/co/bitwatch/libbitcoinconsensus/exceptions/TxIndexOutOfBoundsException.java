package co.bitwatch.libbitcoinconsensus.exceptions;

/**
 * Exception for invalid transaction input index
 */
public class TxIndexOutOfBoundsException extends IndexOutOfBoundsException {

    public TxIndexOutOfBoundsException() {
        super();
    }

    public TxIndexOutOfBoundsException(String message) {
        super(message);
    }

}
