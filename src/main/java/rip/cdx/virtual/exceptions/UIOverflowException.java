package rip.cdx.virtual.exceptions;

public class UIOverflowException extends RuntimeException {
    public UIOverflowException() {
        super("User interface has been overflowed");
    }
}
