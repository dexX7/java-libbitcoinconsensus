package co.bitwatch.libbitcoinconsensus.exceptions;

/**
 * Exception for deserialization failures
 */
public class DeserializeException extends IllegalArgumentException {

    public DeserializeException() {
        super();
    }

    public DeserializeException(String message) {
        super(message);
    }

}
