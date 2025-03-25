package rip.cdx.virtual.exceptions;

import rip.cdx.virtual.ui.component.UIComponent;

public class SlotReservedException extends RuntimeException {
    public SlotReservedException(UIComponent holder, UIComponent overrider) {
        super(overrider + " attempted to reserve slot held by " + holder);
    }
}
