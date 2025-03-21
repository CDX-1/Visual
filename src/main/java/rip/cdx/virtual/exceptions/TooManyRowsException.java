package rip.cdx.virtual.exceptions;

public class TooManyRowsException extends RuntimeException {
    public TooManyRowsException(int maxRows) {
        super("Specified rows for UI exceeds maximum number of rows: " + maxRows);
    }
}
