package rip.cdx.virtual.exceptions;

public class InventoryOverflowException extends RuntimeException {
    public InventoryOverflowException(String message) {
        super(message);
    }
}
